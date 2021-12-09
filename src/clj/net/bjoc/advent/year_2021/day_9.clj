(ns net.bjoc.advent.year-2021.day-9
  (:use [net.bjoc.advent.util.numeric :only [parse-int]])
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.core :as advent]))

(defn file->matrix [filename]
  (->> filename
       slurp
       mtx/from-string
       (reduce-kv (fn [m k v] (assoc m k (-> v str parse-int))) {})))

(defn neighbors [[x y] & {:keys [x-lower x-upper y-lower y-upper] :as bounds}]
  (->> [[(inc x) y] [(dec x) y] [x (inc y)] [x (dec y)]]
       (filter (fn [[x y]]
                 (and (or (not x-lower)
                          (>= x x-lower))
                      (or (not x-upper)
                          (<= x x-upper))
                      (or (not y-lower)
                          (>= y y-lower))
                      (or (not y-upper)
                          (<= y y-upper)))))))

(defn low-point-fn [matrix]
  (fn [[xy v]]
    (->> (neighbors xy)
         (map matrix)
         (remove nil?)
         (every? #(> % v)))))

(defn file->risk-sum [filename]
  (let [matrix (file->matrix filename)
        low-point? (low-point-fn matrix)]
    (->> matrix
         (filter low-point?)
         vals
         (map inc)
         (reduce +))))

;;;

(defn basin [m [xy v]]
  (let [[x-lim y-lim] (map dec (mtx/size m))
        neighbors* #(neighbors % :x-lower 0 :x-upper x-lim
                               :y-lower 0 :y-upper y-lim)]
    (loop [basin #{xy}
           candidates (mapcat neighbors* basin)]
      (if (empty? candidates)
        basin
        (let [new-basin (filter (fn [xy]
                                  (let [v (m xy)]
                                    (and v
                                         (< v 9)
                                         (->> (neighbors* xy)
                                              (remove basin)
                                              (map m)
                                              (every? #(>= % v))))))
                                candidates)
              updated-basin (into basin new-basin)]
          (recur updated-basin
                 (->> (mapcat neighbors* new-basin)
                      (remove basin))))))))

(defn basin-size [matrix low-point]
  (count (basin matrix low-point)))

(defn file->largest-basins-product [filename]
  (let [matrix (file->matrix filename)
        low-point? (low-point-fn matrix)]
    (->> matrix
         (filter low-point?)
         (map (partial basin-size matrix))
         (sort >)
         (take 3)
         (reduce *))))

;;;

(advent/defrunner 1 file->risk-sum "Risk sum")
(advent/defrunner 2 file->largest-basins-product "Product of 3 largest basins")

;;;

(comment
  (require '[net.bjoc.advent.util.matrix-visualization :as vis])
  (import '[java.awt Color])

  (let [filename "data/year_2021/day_9.input"
        matrix (file->matrix filename)
        low-point? (low-point-fn matrix)
        rand-scaled-color-fn (fn []
                               (let [hue (rand)
                                     sat (+ 0.45 (rand 0.5))]
                                 (fn [depth]
                                   (. Color getHSBColor hue sat
                                      (-> depth (/ 9.0) (* 0.6) (+ 0.3))))))
        basin-map (->> matrix
                       (filter low-point?)
                       (map (partial basin matrix))
                       (sort-by #(- (count %)))
                       (map (fn [basin]
                              (let [color-fn (rand-scaled-color-fn)]
                                (map (fn [xy] [color-fn xy]) basin))))
                       (apply concat)
                       (reduce (fn [m [color-fn xy]]
                                 (assoc m xy (color-fn (matrix xy))))
                               {}))]
    (vis/write-image-file basin-map "visualizations/2021_day_9.png"
                          :type :colors :background-color (. Color darkGray)
                          :img-size [800 800]))
  )
