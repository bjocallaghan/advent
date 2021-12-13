(ns net.bjoc.advent.year-2021.day-12
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))

(def big-cave? #(some (fn [c] (Character/isUpperCase c)) %))
(def start? #(= "start" %))
(def end? #(= "end" %))
(def small-cave? (every-pred (complement big-cave?)
                             (complement start?)
                             (complement end?)))

(defn file->cave-map [filename]
  (dissoc (->> filename
              slurp
              (#(str/split % #"\n"))
              (map #(str/split % #"-"))
              (reduce (fn [m [loc-1 loc-2]]
                        (-> m
                            (update loc-1 conj loc-2)
                            (update loc-2 conj loc-1)))
                      {})
              (reduce-kv (fn [m k v] (assoc m k (remove start? v))) {}))
          "end"))

(defn valid-dest-fn [path]
  (fn [dest]
    (or (big-cave? dest)
        (end? dest)
        (and (small-cave? dest)
             (not ((set path) dest))))))

(defn nexts-fn 
  ([cave-map] (nexts-fn cave-map valid-dest-fn))
  ([cave-map valid-pred]
   (fn [path]
     (let [curr (last path)]
       (if (end? curr)
         [path]
         (let [valid-dest? (valid-pred path)]
           (->> (cave-map curr)
                (filter valid-dest?)
                (map #(conj path %)))))))))

(defn ended? [path] (-> path last end?))

(defn file->num-paths [filename]
  (let [cave-map (file->cave-map filename)
        nexts (nexts-fn cave-map)]
    (loop [paths [["start"]]]
      (if (every? ended? paths)
        (count paths)
        (recur (mapcat nexts paths))))))

;;;

(defn has-double-small? [path]
  (->> path
       (filter small-cave?)
       frequencies
       vals
       (some #{2})))

(defn valid-dest-fn* [path]
  (fn [dest]
    (or (big-cave? dest)
        (end? dest)
        (and (small-cave? dest)
             (let []
               (or (not ((set path) dest))
                   (not (has-double-small? path))))))))

(defn file->num-paths* [filename]
  (let [cave-map (file->cave-map filename)
        nexts (nexts-fn cave-map valid-dest-fn*)]
    (loop [paths [["start"]]]
      (if (every? ended? paths)
        (count paths)
        (recur (mapcat nexts paths))))))

;;;

(advent/defrunner 1 file->num-paths "Number of paths")
(advent/defrunner 2 file->num-paths* "Number of paths (modified algorithm)")
