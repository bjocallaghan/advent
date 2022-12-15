(ns net.bjoc.advent.year-2022.day-14
  (:require [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.coords :refer [add-coords difference]]
            [net.bjoc.advent.util.misc :refer [file->lines zip find-first]]))

(defn expand [xy-1 xy-2]
  (let [dir (mapv #(if (zero? %) 0 (/ % (Math/abs %))) (difference xy-2 xy-1))
        inc* (partial add-coords dir)]
    (loop [acc #{}
           xy xy-1]
      (if (= xy xy-2)
        (conj acc xy)
        (recur (conj acc xy) (inc* xy))))))

(defn line->blocks [line]
  (let [points (->> (re-seq #"(-?\d+),(-?\d+)" line)
                   (map rest)
                   (map #(map (fn [x] (Integer/parseInt x)) %))
                   (map vec))
        pairs (zip points (drop 1 points))]
    (set pairs)))

(defn file->blocked [filename]
  (->> filename
       file->lines
       (mapcat line->blocks)
       (mapcat #(apply expand %))
       set))

;; NOTE: After completing part 1, I copy/pasted the solution and did part
;; 2. With only minor modifications, I got part 2 working. Because the code for
;; parts 1 and 2 was so similar, I refactored. However, I sort of hate the
;; result -- the final result (although it works for both parts 1 and 2), seems
;; clunky and unintuitive. Oh well.

(defn add-grain [stop-step-fn stop-all-fn blocked grain]
  (let [free? (complement blocked)
        next-frame #(find-first free? (map (partial add-coords %) [[0 1] [-1 1] [1 1]]))]
    (loop [grain grain]
      (let [next (next-frame grain)]
        (cond
          (stop-all-fn grain next) (reduced (conj blocked grain))
          (or (nil? next) (stop-step-fn next)) (conj blocked grain)
          :else (recur next))))))

(defn file->grain-count [run-type filename]
  (let [origin [500 0]
        blocked (file->blocked filename)
        lowest (apply max (map second blocked))
        floor (+ 2 (apply max (map second blocked)))
        floor? (fn [[_ y]] (= y floor))]
    (- (count
        (reduce (partial add-grain floor? (case run-type
                                            :abyss (fn [_ next] (floor? next))
                                            :floor (fn [curr next] (and (= curr origin) (nil? next)))))
                blocked (repeat origin)))
       (count blocked)
       (case run-type :abyss 1 :floor 0))))

;;;

(advent/defrunner 1 (partial file->grain-count :abyss) "Grain count")
(advent/defrunner 2 (partial file->grain-count :floor) "Grain count with floor")
