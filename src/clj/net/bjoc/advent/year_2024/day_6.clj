(ns net.bjoc.advent.year-2024.day-6
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.coords :refer [add-coords]]
            [net.bjoc.advent.util.matrix :as mat]))

(defn start-position [m]
  (->> m
       (filter (fn [[_ v]] (= v \^)))
       first
       key))

(defn turn [dir]
  (->> (cycle [[0 1] [-1 0] [0 -1] [1 0]])
       (drop-while #(not= dir %))
       rest
       first))

(def start-dir [0 -1])

(defn start-state [m]
  (let [pos (start-position m)]
    {:visited #{pos}
     :visited* #{[pos start-dir]}
     :pos pos
     :dir start-dir
     :m (assoc m pos \.)
     :looped? false
     :running? true}))

(defn evolve [{:keys [pos dir m visited*] :as state}]
  (let [maybe-next (add-coords pos dir)]
    (case (m maybe-next)
      \. (let [maybe-next* [maybe-next dir]
               looped? (boolean (visited* maybe-next*))]
           (-> state
               (assoc :pos maybe-next)
               (assoc :running? (not looped?))
               (assoc :looped? looped?)
               (update :visited conj maybe-next)
               (update :visited* conj maybe-next*)))
      \# (update state :dir turn)
      ;; else off the map
      (update state :running? not))))

(defn evolutions [state]
  (lazy-seq (cons state (when (:running? state) (evolutions (evolve state))))))

(def run #(first (drop-while :running? (evolutions %))))
(def file->visted-count #(-> % mat/from-file start-state run :visited count))

;;;

(defn safe-insertions-count [m]
  (->> (keep (fn [[k v]] (when (= \. v) k)) m)
       (map #(assoc m % \#))
       (map start-state)
       (map run)
       (filter :looped?)
       count))

(def file->safe-insertions-count (comp safe-insertions-count mat/from-file))

;;;

(advent/defrunner 1 file->visted-count "Visited count")
(advent/defrunner 2 file->safe-insertions-count "Safe insertions count")
