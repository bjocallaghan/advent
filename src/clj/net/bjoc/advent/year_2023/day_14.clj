(ns net.bjoc.advent.year-2023.day-14
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.util.matrix-visualization :as vis]
            [net.bjoc.advent.util.misc :refer []]
            ))

(defn empty? [[_ v]] (= v \.))
(defn round? [[_ v]] (= v \O))
(defn cube? [[_ v]] (= v \#))

(defn col-load [depth col-elements]
  (let [y->load (fn [y] (- depth y))]
    (:load
     (reduce (fn [{:keys [current-cube load] :as state} el]
               (if (cube? el)
                 (let [[[_ y] _] el]
                   (assoc state :current-cube y))
                 (-> state
                     (update :load + (-> current-cube inc y->load))
                     (update :current-cube inc))))
             {:load 0 :current-cube -1} (sort col-elements)))))

(defn calc-load [m]
  (let [[x-size y-size] (mtx/size m)
        col-load* (partial col-load y-size)
        m* (remove empty? m)
        cols (->> x-size
                  range
                  (map #(filter (fn [[[x _] _]] (= x %)) m*)))]
    (->> cols
         (map col-load*)
         (reduce +))))

(def file->load (comp calc-load mtx/from-file))

;;;

(defn slide-col [col-elements]
  (:elements
   (reduce (fn [{:keys [elements current-y]} this]
             (let [[[this-x this-y] this-v] this]
               (if (cube? this)
                 {:elements (conj elements this)
                  :current-y this-y}
                 {:elements (conj elements [[this-x (inc current-y)] this-v])
                  :current-y (inc current-y)})))
           {:current-y -1}
           (sort col-elements))))

(defn slide-up [m]
  (let [[x-size y-size] (mtx/size m)
        m* (remove empty? m)
        cols (->> x-size
                  range
                  (map #(filter (fn [[[x _] _]] (= x %)) m*)))]
    (->> cols
         (mapcat slide-col)
         (into {})
         )))

(defn tilt [dir m]
  (case dir
    :north (-> m slide-up)
    :east (-> m (mtx/rotate 3) slide-up (mtx/rotate -3))
    :south (-> m (mtx/rotate 2) slide-up (mtx/rotate -2))
    :west (-> m (mtx/rotate 1) slide-up (mtx/rotate -1))))

(defn tilt-cycle [m]
  (->> m
       (tilt :north)
       (tilt :west)
       (tilt :south)
       (tilt :east)))

(defn find-period [m]
  (loop [m m
         counter 0
         known? {m 0}]
    (let [next (tilt-cycle m)]
      (when (> counter 500)
        (throw (ex-info "Bailing. Didn't detect loop yet." {:tries counter})))
      (if (known? next)
        {:status :loop-detected
         :period (- (inc counter) (known? next))
         :offset-matrix next
         :offset (known? next)
         }
        (recur next
               (inc counter)
               (conj known? [next (inc counter)]))))))

(defn calc-load-2 [m]
  (let [[_ y-size] (mtx/size m)
        y->load (fn [y] (- y-size y))]
    (->> m
         (filter round?)
         (map (fn [[[_ y] _]] y))
         (map y->load)
         (reduce +))))

(defn file->spin-load [filename]
  (let [m (mtx/from-file filename)
        {:keys [period offset offset-matrix]} (find-period m)
        remaining-tilts (-> 1000000000 (- offset) (mod period) inc)]
    (->> offset-matrix
         (iterate tilt-cycle)
         (take remaining-tilts)
         last
         calc-load-2)))

;;;

(comment
  (let [filename "data/year_2023/day_14_example1.input"
        my-pallette {\# java.awt.Color/BLACK
                     \O java.awt.Color/GRAY
                     \. java.awt.Color/WHITE}]
    (->> (mtx/from-file filename)
         ;; find-period))
         (iterate tilt-cycle)
         (take 20)
         (map calc-load-2)
         ))

  (let [filename "data/year_2023/day_14_example1.input"
        my-pallette {\# java.awt.Color/BLACK
                     \O java.awt.Color/GRAY
                     \. java.awt.Color/WHITE}
        show* #(vis/show % :pallette my-pallette)]
    (->> (mtx/from-file filename)
         ;; find-period))
         (iterate tilt-cycle)
         (take 5)
         (map show*)
         ))
  )

;;;

(advent/defrunner 1 file->load "Load")
(advent/defrunner 2 file->spin-load "Load after spin cycles")
