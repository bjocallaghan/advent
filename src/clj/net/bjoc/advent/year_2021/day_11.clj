(ns net.bjoc.advent.year-2021.day-11
  (:use [net.bjoc.advent.util.numeric :only [parse-int]]
        [net.bjoc.advent.util.coords :only [add-coords]])
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.matrix :as mtx]))

(defn file->octopuses [filename]
  (->> filename
       slurp
       mtx/from-string
       (reduce-kv #(assoc %1 %2 (-> %3 str parse-int)) {})))

(defn neighbors
  [{:keys [x-lower y-lower x-upper y-upper]
    :or {x-lower 0
         y-lower 0}
    :as bounds} xy]
  (let [in-bounds? (fn [[x y]] (and (>= x x-lower)
                                    (>= y y-lower)
                                    (<= x x-upper)
                                    (<= y y-upper)))]
    (->> [[-1 -1] [-1 0] [-1 1] [0 -1] [0 1] [1 -1] [1 0] [1 1]]
         (map (partial add-coords xy))
         (filter in-bounds?))))

(defn step
  ([world] (step world 1))
  ([{:keys [octopuses flashes] :or {flashes 0} :as world} num-steps]
   (if (zero? num-steps)
     world
     (recur (let [[x-upper y-upper] (mtx/size octopuses)
                  neighbors* (partial neighbors {:x-upper (dec x-upper)
                                                 :y-upper (dec y-upper)})
                  flash (fn [m xy]
                          (reduce (fn [m xy] (update m xy inc)) m (neighbors* xy)))
                  unflash (fn [m] (into {} (map (fn [[k v]] [k (if (>= v 10) 0 v)]) m)))
                  flashing? (fn [[xy v]] (>= v 10))]
              (loop [m (reduce (fn [m [k v]] (assoc m k (inc v))) {} octopuses)
                     flashing #{}]
                (let [new-flashing (set/difference (->> m 
                                                        (filter flashing?) 
                                                        keys 
                                                        (into #{}))
                                                   flashing)]
                  (if (empty? new-flashing)
                    {:octopuses (unflash m)
                     :flashes (+ flashes (count flashing))
                     :step-flashes (count flashing)}
                    (recur (reduce flash m new-flashing)
                           (set/union flashing new-flashing))))))
            (dec num-steps)))))

(defn file->post-steps-flash-count
  ([filename] (file->post-steps-flash-count filename 100))
  ([filename num-steps]
   (-> {:octopuses (file->octopuses filename)}
       (step num-steps)
       :flashes)))

;;;

(defn file->flash-sync [filename]
  (let [octopuses (file->octopuses filename)
        num-octopuses (count octopuses)]
  (loop [world {:octopuses octopuses}
         num-steps 0]
    (if (= 100 (:step-flashes world))
      num-steps
      (recur (step world) (inc num-steps))))))

;;;

(advent/defrunner 1 file->post-steps-flash-count "Flashes after 100 steps")
(advent/defrunner 2 file->flash-sync "Steps until flash sync")
