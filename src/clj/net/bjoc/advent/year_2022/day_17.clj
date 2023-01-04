(ns net.bjoc.advent.year-2022.day-17
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.coords :refer [add-coords]]
            [net.bjoc.advent.util.logging :as log]
            [net.bjoc.advent.util.misc :refer [zip]]))

(def pieces
  [[[0 0] [1 0] [2 0] [3 0]]
   [[1 0] [0 1] [1 1] [2 1] [1 2]]
   [[2 0] [2 1] [0 2] [1 2] [2 2]]
   [[0 0] [0 1] [0 2] [0 3]]
   [[0 0] [1 0] [0 1] [1 1]]])

(defn file->gusts [filename]
  (->> filename
       slurp
       (map #(case % \> [1 0] \< [-1 0]))
       cycle))

(defn apply-gust [gust occupied? rock]
  (let [moved-rock (map (partial add-coords gust) rock)]
    (if (and (every? (fn [[x _]] (<= 0 x 6)) moved-rock)
             (not (some occupied? moved-rock)))
      moved-rock
      rock)))

(defn do-rock [{:keys [gusts occupied?] :as state} rock]
  (let [offset [2 (- (apply min (map second occupied?))
                     (apply max (map second rock))
                     4)]]
    (loop [rock (map #(add-coords offset %) rock)
           gusts gusts]
      (let [blown-rock (apply-gust (first gusts) occupied? rock)
            next-rock (map #(add-coords [0 1] %) blown-rock)]
        (if (some occupied? next-rock)
          (-> state
              (assoc :gusts (rest gusts))
              (assoc :occupied? (into occupied? blown-rock)))
          (recur next-rock
                 (rest gusts)))))))

(defn file->height [n filename]
  (let [state {:gusts (file->gusts filename)
               :occupied? (into #{} (for [i (range 7)] [i 0]))}]
    (->> (reduce do-rock state (take n (cycle pieces)))
         :occupied?
         (map second)
         (apply min)
         -)))

;;;

;;;

(advent/defrunner 1 (partial file->height 2022) "Height")
(advent/defrunner 2 identity "")
