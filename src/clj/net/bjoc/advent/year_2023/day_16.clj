(ns net.bjoc.advent.year-2023.day-16
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix :as mtx]))

(def empty?* (fn [[_ v]] (= v \.)))

(def starting-frontier #{[0 0 :right]})

(defn go [[x y] direction]
  (case direction
    :left [(dec x) y :left]
    :right [(inc x) y :right]
    :down [x (inc y) :down]
    :up [x (dec y) :up]))

(defn make-nexts-fn [maze]
  (let [[x-max y-max] (mtx/size maze)
        in-bounds? (fn [[x y _]]
                     (and (< -1 x x-max)
                          (< -1 y y-max)))]
    (fn [[x y dir]]
      (filter in-bounds?
              (case [dir (maze [x y])]
                [:right nil] [(go [x y] :right)]
                [:right \-] [(go [x y] :right)]
                [:right \\] [(go [x y] :down)]
                [:right \/] [(go [x y] :up)]
                [:right \|] [(go [x y] :up) (go [x y] :down)]

                [:up nil] [(go [x y] :up)]
                [:up \|] [(go [x y] :up)]
                [:up \\] [(go [x y] :left)]
                [:up \/] [(go [x y] :right)]
                [:up \-] [(go [x y] :left) (go [x y] :right)]

                [:left nil] [(go [x y] :left)]
                [:left \-] [(go [x y] :left)]
                [:left \\] [(go [x y] :up)]
                [:left \/] [(go [x y] :down)]
                [:left \|] [(go [x y] :up) (go [x y] :down)]

                [:down nil] [(go [x y] :down)]
                [:down \|] [(go [x y] :down)]
                [:down \\] [(go [x y] :right)]
                [:down \/] [(go [x y] :left)]
                [:down \-] [(go [x y] :left) (go [x y] :right)])))))

(defn evolve [{:keys [maze visited frontier nexts-fn] :as state}]
  (let [raw-new-frontier (set (mapcat nexts-fn frontier))]
    (-> state
        (update :steps inc)
        (assoc :frontier (set/difference raw-new-frontier visited))
        (assoc :visited (set/union visited frontier)))))

(defn calc-num-energized [starting-state]
  (->> starting-state
       (iterate evolve)
       (take-while #(not-empty (:frontier %)))
       last
       evolve
       :visited
       (map (fn [[x y _]] [x y]))
       set
       count))

(defn file->maze [filename]
  (->> filename
       mtx/from-file
       (remove empty?*)
       (into {})))

(defn file->num-energized [filename]
  (let [maze (file->maze filename)]
    (calc-num-energized {:steps 0
                         :visited #{}
                         :frontier starting-frontier
                         :nexts-fn (make-nexts-fn maze)})))

;;;

(defn file->best-energized [filename]
  (let [maze (file->maze filename)
        [max-x max-y] (mtx/size maze)
        partial-starting-state {:steps 0
                                :visited #{}
                                :nexts-fn (make-nexts-fn maze)}
        possible-starting-frontiers (concat
                                     (map (fn [x] #{[x 0 :down]}) (range max-x))
                                     (map (fn [x] #{[x (dec max-y) :up]}) (range max-x))
                                     (map (fn [y] #{[0 y :right]}) (range max-y))
                                     (map (fn [y] #{[(dec max-x) y :left]}) (range max-y)))]
    (->> possible-starting-frontiers
         (map #(-> partial-starting-state (merge {:frontier %}) calc-num-energized))
         (apply max))))

;;;

(advent/defrunner 1 file->num-energized "Tiles energized")
(advent/defrunner 2 file->best-energized "Best energized output")
