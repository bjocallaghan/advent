(ns net.bjoc.advent.year-2023.day-12
  (:require [clojure.string :as str]
            [clojure.math.combinatorics :refer [combinations]]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(defn parse-line [line]
  (let [[s raw-spec] (str/split line #" ")]
    {:spec (->> (str/split raw-spec #",") (map parse-long))
     :conditions s}))

(def unknown? #(= % \?))
(def operational? #(= % \.))
(def damaged? #(= % \#))

(defn num-missing [{:keys [spec conditions]}]
  (- (reduce + spec)
     (->> conditions
          (filter damaged?)
          count)))

(defn replace-at [s idx c]
  (str (subs s 0 idx) c (subs s (inc idx))))

(defn conditions->spec [conditions]
  (->> (str/split conditions #"\.")
       (remove empty?)
       (map count)))

(defn valid-expansions [{:keys [conditions spec] :as line}]
  (let [unknown-indexes (->> conditions
                             (map-indexed (fn [i c] (when (unknown? c) i)))
                             (remove nil?))
        fill #(reduce (fn [s idx] (replace-at s idx \#)) (str/replace conditions #"\?" ".") %)]
    (->> (combinations unknown-indexes (num-missing line))
         (map fill)
         (filter #(= (conditions->spec %) spec)))))

(defn line->num-expansions [line]
  (->> line
       parse-line
       valid-expansions
       count))

(defn file->num-expansions-sum [filename]
  (->> filename
       file->lines
       (map line->num-expansions)
       (reduce +)))
  
;;;

(defn parse-line-2 [line]
  (let [{:keys [conditions spec]} (parse-line line)]
    {:spec (apply concat (repeat 5 spec))
     :conditions (str/join "?" (repeat 5 conditions))}))

(defn line->num-expansions-2 [line]
  (->> line
       parse-line-2
       valid-expansions
       count))

(defn file->num-expansions-sum-2 [filename]
  (->> filename
       file->lines
       (map line->num-expansions-2)
       (reduce +)))
  
;;;

(advent/defrunner 1 file->num-expansions-sum "Sum of possibilities")
(advent/defrunner 2 file->num-expansions-sum-2 "Sum of possibilities (unfolded)")
