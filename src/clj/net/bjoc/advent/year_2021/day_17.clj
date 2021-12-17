(ns net.bjoc.advent.year-2021.day-17
  (:use [net.bjoc.advent.util.coords :only [add-coords]]
        [net.bjoc.advent.util.misc :only [take-until]]
        [net.bjoc.advent.util.numeric :only [parse-int]])
  (:require [net.bjoc.advent.core :as advent]))

(defn file->target [filename]
  (let [[_ x-min x-max y-min y-max] (re-find #"x=(-?\d+)..(-?\d+), y=(-?\d+)..(-?\d+)$"
                                             (slurp filename))]
    (reduce-kv #(assoc %1 %2 (parse-int %3)) {}
               {:x-min x-min
                :x-max x-max
                :y-min y-min
                :y-max y-max})))

(defn in-target-pred [{:keys [x-min x-max y-min y-max] :as target}]
  (fn [{:keys [position] :as state}]
    (let [[x y] position]
      (and (<= x-min x x-max)
           (<= y-min y y-max)))))

(defn below-floor-pred [{:keys [y-min] :as target}]
  (fn [{:keys [position] :as state}]
    (let [[_ y] position]
      (< y y-min))))

(def starting-position [0 0])

(defn dec-velocity [[delta-x delta-y]]
  [(cond
     (zero? delta-x) 0
     (pos? delta-x) (dec delta-x)
     (neg? delta-y) (inc delta-x))
   (dec delta-y)])

(defn step [{:keys [position velocity] :as state}]
  (-> state
      (update :position add-coords velocity)
      (update :velocity dec-velocity)))

(defn steps [state]
  (lazy-seq (cons state (steps (step state)))))

(defn valid-velocity-pred [target]
  (let [in-target? (in-target-pred target)
        below-floor? (below-floor-pred target)]
    (fn [velocity]
      (->> {:position starting-position :velocity velocity}
           steps
           (take-until (some-fn in-target? below-floor?))
           last
           in-target?))))

(defn min-x-velocity [{:keys [x-min] :as target}]
  (loop [a 0]
    (if (>= (reduce + (range a 0 -1)) x-min)
      a
      (recur (inc a)))))

(defn max-x-velocity [{:keys [x-max] :as target}] x-max)
(defn min-y-velocity [{:keys [y-min] :as target}] y-min)
(defn max-y-velocity [{:keys [y-min] :as target}] (Math/abs y-min))

(defn all-valid-velocities [target]
  (let [valid-velocity? (valid-velocity-pred target)
        velocities (for [delta-x (range (-> target min-x-velocity)
                                        (-> target max-x-velocity inc))
                         delta-y (range (-> target min-y-velocity)
                                        (-> target max-y-velocity inc))]
                     [delta-x delta-y])]
    (->> velocities
         (filter valid-velocity?))))

(defn velocity-max-height [velocity]
  (let [[_ max-y] (->> {:position starting-position :velocity velocity}
                       steps
                       (drop-while #(-> % :velocity second pos?))
                       first
                       :position)]
    (or max-y 0)))

(defn file->find-best [filename]
  (let [target (file->target filename)]
    (->> (all-valid-velocities target)
         (map velocity-max-height)
         (apply max))))

;;;

(defn file->count-valid [filename]
  (let [target (file->target filename)]
    (count (all-valid-velocities target))))

;;;

(advent/defrunner 1 file->find-best "Best achievable height")
(advent/defrunner 2 file->count-valid "Number of possible velocities")
