(ns net.bjoc.advent.year-2016.day-8
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]
            [net.bjoc.advent.util.matrix :as matrix]
            [net.bjoc.advent.util.matrix-visualization :as vis]))

(def rect-regex #"^rect (\d+)x(\d+)$")
(def rotate-regex #"^rotate (?:column|row) (x|y)=(\d+) by (\d+)$")

(defn parse-line [line]
  (or
   (when-let [[_ x y] (re-find rect-regex line)]
     {:operation :rect
      :dimensions [(parse-long x) (parse-long y)]})
   (when-let [[_ axis i mag] (re-find rotate-regex line)]
     {:operation :rotate
      :axis (case axis "x" 0 "y" 1)
      :index (parse-long i)
      :magnitude (parse-long mag)})))

(defn init-matrix [[x-size y-size] initial-value]
  (into {} (for [x (range x-size)
                 y (range y-size)]
             [[x y] initial-value])))

(defn apply-rect-instruction [m {dimensions :dimensions}]
  (merge m (init-matrix dimensions \#)))

(defn apply-rotation-instruction [m {:keys [axis index magnitude]}]
  (let [is-x (zero? axis)
        div (get (matrix/size m) (if is-x 1 0))
        inc* (fn [[x y]] 
               (assoc [x y] (if is-x 1 0) (-> (if is-x y x) (+ magnitude) (mod div))))]
    (->> m
         (map (fn [[coords v]]
                (if (= index (get coords axis))
                  [(inc* coords) v]
                  [coords v])))
         (into {}))))

(defn apply-instruction [m {operation :operation :as instruction}]
  (case operation
    :rect (apply-rect-instruction m instruction)
    :rotate (apply-rotation-instruction m instruction)))

(defn file->screen [dimensions filename]
  (->> filename
       file->lines
       (map parse-line)
       (reduce apply-instruction (init-matrix dimensions \.))))

(def screen-dimensions [50 6])
(def lit? (fn [[_ v]] (= v \#)))
     
(defn count-lit-pixels [filename]
  (->> filename
       (file->screen screen-dimensions)
       (filter lit?)
       count))

;;;

(defn render-screen [input-filename dest-filename]
  (let [m (->> input-filename

               (file->screen screen-dimensions))]
    (vis/write-image-file m dest-filename)
    (format "Successfully wrote to %s" dest-filename)))

;;;

(advent/defrunner 1 count-lit-pixels "Number of lit pixels")
(advent/defrunner 2 render-screen "Writing" "visualizations/2016_day_8.png")
