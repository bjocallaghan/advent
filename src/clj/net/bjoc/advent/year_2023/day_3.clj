(ns net.bjoc.advent.year-2023.day-3
  (:require [clojure.string :as str]
            [clojure.set :refer [intersection union]]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.util.coords :refer [add-coords]]))

(defn period? [[_ v]] (= \. v))
(defn digit? [[_ v]] (re-find #"\d" (str v)))

(defn file->schematic [filename]
  (->> filename
       mtx/from-file
       (remove period?)
       (into {})))

(defn neighbors [coord]
  (set (map (partial add-coords coord)
            [[-1 -1] [-1  0] [-1  1] [ 0 -1]
             [ 0  1] [ 1 -1] [ 1  0] [ 1  1]])))

(defn island-neighbors [island] (->> island (mapcat neighbors) set))

(defn islands [matrix]
  ;; a mix of clumsy and elegant
  (loop [islands (->> matrix
                      keys
                      (map (fn [x] #{x})))]
    (let [new-islands
          (->> islands
               (map (fn [island]
                      (let [perimeter (island-neighbors island)
                            other-islands (remove #(= island %) islands)]
                        (->> other-islands
                             (filter #(not-empty (intersection % perimeter)))
                             (apply (partial union island))
                             ))))
               set)]
      (if (= new-islands islands)
        islands
        (recur new-islands)))))

(defn digit-island-value [m coords]
  (->> coords
       sort
       (map m)
       (apply str)
       parse-long))

(defn part-number-sum [filename]
  (let [m (file->schematic filename)
        islands (->> m (filter digit?) islands)
        symbols (->> m (remove digit?) keys set)]
    (->> islands
         (filter #(not-empty (intersection symbols (island-neighbors %))))
         (map (partial digit-island-value m))
         (reduce +))))

;;;

(defn asterisk? [[_ v]] (= v \*))

(defn gear-ratio-sum [filename]
  (let [m (file->schematic filename)
        islands (->> m (filter digit?) islands)
        asterisk-coords (->> m (filter asterisk?) keys set)
        island-value (partial digit-island-value m)]
    (->> asterisk-coords
         (map (fn [coord]
                (filter (fn [island]
                          (not-empty (intersection (neighbors coord) island)))
                        islands)))
         (filter #(= 2 (count %)))
         (map #(->> % (map island-value) (reduce *)))
         (reduce +))))

;;;

(advent/defrunner 1 part-number-sum "Part number sum")
(advent/defrunner 2 gear-ratio-sum "Gear ratio sum")
