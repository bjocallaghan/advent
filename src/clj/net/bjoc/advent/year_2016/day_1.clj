(ns net.bjoc.advent.year-2016.day-1
  (:use [net.bjoc.advent.util.numeric :only [parse-int]]
        [net.bjoc.advent.util.coords :only [add-coords]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))

(defn file->commands [filename]
  (->> filename
       slurp
       (#(str/split % #", "))
       (map (fn [s]
              {:turn (case (first s) \R :right \L :left)
               :magnitude (parse-int (subs s 1))}))))

(def start-location {:x 0 :y 0 :heading [0 1]})

(defn change-heading [current turn]
  (get (case turn
         :left {[0 1] [-1 0]
                [1 0] [0 1]
                [0 -1] [1 0]
                [-1 0] [0 -1]}
         :right {[0 1] [1 0]
                 [1 0] [0 -1]
                 [0 -1] [-1 0]
                 [-1 0] [0 1]})
       current))

(defn execute
  [{:keys [x y heading] :as location} {:keys [turn magnitude] :as command}]
  (let [new-heading (change-heading heading turn)
        delta (apply add-coords (repeat magnitude new-heading))
        [new-x new-y] (add-coords [x y] delta)]
    (-> location
        (assoc :heading new-heading)
        (assoc :x new-x)
        (assoc :y new-y))))

(defn filename->distance [filename]
  (let [{:keys [x y]} (->> filename
                           file->commands
                           (reduce execute start-location))]
    (+ (Math/abs x) (Math/abs y))))

;;;

(defn unwrap [{:keys [magnitude] :as command} heading]
  (repeat magnitude heading))

(defn walk-until [commands]
  (let [turns (map :turn commands)
        [_ headings] (reduce (fn [[curr headings] turn]
                               (let [new-heading (change-heading curr turn)]
                                 [new-heading (conj headings new-heading)]))
                             [[0 1] []] turns)]
    (loop [steps (mapcat unwrap commands headings)
           current [0 0]
           visited #{[0 0]}]
      (when-not (empty? steps)
        (let [next (add-coords current (first steps))]
          (if (visited next)
            next
            (recur (rest steps) next (conj visited next))))))))

(defn filename->walk-until [filename]
  (let [[x y] (-> filename
                  file->commands
                  walk-until)]
    (+ (Math/abs x) (Math/abs y))))

;;;

(advent/defrunner 1 filename->distance "Direct distance (in blocks)")
(advent/defrunner 2 filename->walk-until "Doubleback distance (in blocks)")
