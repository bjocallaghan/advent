(ns net.bjoc.advent.year-2022.day-12
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix :as mtx]
            [net.bjoc.advent.util.coords :refer [add-coords]]
            [net.bjoc.advent.util.misc :refer [single]]))

(defn neighbors [xy] (map (partial add-coords xy) [[1 0] [-1 0] [0 1] [0 -1]]))

(defn file->state [filename]
  (let [matrix (mtx/from-file filename)
        start (->> matrix (filter (fn [[_ v]] (= v \S))) single first)
        end (->> matrix (filter (fn [[_ v]] (= v \E))) single first)]
    {:end? #(= % end)
     :accessible? (let [m (mtx/update-all int (-> matrix (assoc start \a) (assoc end \z)))]
                    (fn [src-xy dest-xy] (<= (dec (m dest-xy)) (m src-xy))))
     :starts (conj (keep (fn [[xy v]] (when (= v \a) xy)) matrix) start)
     :unexplored (-> matrix keys set)}))

(defn path-length [{:keys [end? accessible? unexplored] :as state} start]
  (loop [steps 0
         frontier #{start}
         unexplored unexplored]
    (if-let [x (cond
                 (> steps 30000) :too-long
                 (some end? frontier) steps 
                 (empty? frontier) :stuck)]
      x
      (let [next-frontier (reduce (fn [acc xy]
                                    (reduce conj acc
                                            (filter (every-pred unexplored
                                                                (partial accessible? xy))
                                                    (neighbors xy))))
                                  #{} frontier)]
        (recur (inc steps)
               next-frontier
               (reduce disj unexplored next-frontier))))))

(defn file->path-length [filename]
  (let [{:keys [starts] :as state} (file->state filename)]
    (path-length state (first starts))))

;;

(defn file->best-path-length [filename]
  (let [{:keys [starts] :as state} (file->state filename)]
    (->> starts
         (map (partial path-length state))
         (remove #(= :stuck %))
         (apply min))))

;;;

(advent/defrunner 1 file->path-length "Path length")
(advent/defrunner 2 file->best-path-length "Shortest path length")
