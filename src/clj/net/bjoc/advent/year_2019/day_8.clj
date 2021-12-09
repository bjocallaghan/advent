(ns net.bjoc.advent.year-2019.day-8
  (:use [net.bjoc.advent.util.numeric :only [parse-int]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix-visualization :as vis]))

(defn file->layers
  [filename
   & {:keys [width height]
      :or {width 25
           height 6}
      :as options}]
  (->> filename
       slurp
       (map str)
       (map parse-int)
       (partition (* width height))))

(defn file->best-layer-score [filename & {:as options}]
  (->> (apply (partial file->layers filename) (mapcat identity options))
       (sort-by #(count (filter zero? %)))
       first
       frequencies
       (#(* (% 1) (% 2)))))

;;;

(defn layer->matrix
  [layer
   & {:keys [width height]
      :or {width 25
           height 6}
      :as options}]
  (into {}
        (for [x (range width)
              y (range height)]
          [[x y] (->> layer (drop (+ (* y width) x)) first)])))

(defn is-transparent [n] (= 2 n))

(defn compress-matrices
  [matrices
   & {:keys [width height]
      :or {width 25
           height 6}
      :as options}]
  (into {}
        (for [x (range width)
              y (range height)]
          [[x y] (->> matrices
                      (map #(get % [x y]))
                      (drop-while is-transparent)
                      first)])))

(defn file->matrix [filename & {:as options}]
  (let [opts (mapcat identity options)
        file->layers* #(apply (partial file->layers %) opts)
        layer->matrix* #(apply (partial layer->matrix %) opts)
        compress-matrices* #(apply (partial compress-matrices %) opts)]
    (->> filename
         file->layers*
         (map layer->matrix*)
         compress-matrices*)))

(defn file->write-matrix [filename dest-filename]
  (let [m (->> filename
               file->matrix
               (reduce-kv (fn [m k v] (assoc m k (char v))) {})
               (into {}))]
    (vis/write-image-file m dest-filename)
    (format "Successfully wrote to %s" dest-filename)))

;;;

(advent/defrunner 1 file->best-layer-score "'Best' layer's 'score'")
(advent/defrunner 2 file->write-matrix "Writing" "visualizations/2019_day_8.png")
