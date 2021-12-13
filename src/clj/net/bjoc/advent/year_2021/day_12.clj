(ns net.bjoc.advent.year-2021.day-12
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))

(defn file->cave-map [filename]
  (->> filename
       slurp
       (#(str/split % #"\n"))
       (map #(str/split % #"-"))
       (reduce (fn [m [loc-1 loc-2]]
                 (-> m
                     (update loc-1 conj loc-2)
                     (update loc-2 conj loc-1)))
               {})))

(def big-cave? #(some (fn [c] (Character/isUpperCase c)) %))
(def start? #(= "start" %))
(def end? #(= "end" %))
(def small-cave? (every-pred (complement big-cave?)
                             (complement start?)
                             (complement end?)))
(def path-ended? #(-> % last end?))

(defn scrub-map [cave-map cave]
  (if (big-cave? cave)
    cave-map
    (reduce (fn [m [k locs]] (assoc m k (remove #{cave} locs))) {} cave-map)))

(defn nexts [{:keys [path-so-far cave-map] :as state}]
  (let [curr (last path-so-far)]
    (for [next (cave-map curr)]
      (-> state
          (update :path-so-far conj next)
          (update :cave-map scrub-map curr)))))

(defn file->starting-state [filename & has-extra]
  {:path-so-far ["start"]
   :cave-map (-> filename
                 file->cave-map)
   :extra-visit has-extra})

(def halted? #(-> % :path-so-far last end?))
(def stuck? #(empty? ((:cave-map %) (-> % :path-so-far last))))

(defn advance-state [state]
  (cond
    (stuck? state) (throw (ex-info "Can't advance stuck state." state))
    (halted? state) [state]
    :default (nexts state)))

(defn file->num-paths [filename]
  (loop [states [(file->starting-state filename)]]
    (if (every? halted? states)
      (count states)
      (recur (->> states
                  (mapcat advance-state)
                  (remove stuck?))))))

;;;

(defn nexts [{:keys [path-so-far cave-map extra-visit] :as state}]
  (let [curr (last path-so-far)]
    (concat
     (for [next (cave-map curr)]
       (-> state
           (update :path-so-far conj next)
           (update :cave-map scrub-map curr)))
     (when (and extra-visit (small-cave? curr))
       (for [next (cave-map curr)]
         (-> state
             (update :path-so-far conj next)
             (update :extra-visit not)))))))

(defn file->num-paths* [filename]
  (loop [states [(file->starting-state filename true)]]
    (if (every? halted? states)
      (->> states
           (map :path-so-far)
           (map #(str/join "," %))
           (into #{})
           count)
      (recur (->> states
                  (mapcat advance-state)
                  (remove stuck?))))))

;;;

(advent/defrunner 1 file->num-paths "Number of paths")
(advent/defrunner 2 file->num-paths* "Number of paths (modified algorithm)")
