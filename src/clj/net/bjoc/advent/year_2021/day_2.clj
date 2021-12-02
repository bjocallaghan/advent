(ns net.bjoc.advent.year-2021.day-2
  (:require [clojure.string :as str]))

(defn string->command [s]
  (let [[a b] (str/split s #" ")]
    [(keyword a) (Integer/parseInt b)]))

(defn file->commands [filename]
  (->> (str/split (slurp filename) #"\n")
       (map string->command)))

(defn execute [{:keys [horizontal depth] :as location} [instruction magnitude]]
  (cond
    (= instruction :forward) (update location :horizontal #(+ % magnitude))
    (= instruction :down) (update location :depth #(+ % magnitude))
    (= instruction :up) (update location :depth #(- % magnitude))
    :else location))

(def starting-location {:horizontal 0 :depth 0 :aim 0})

(defn file->location-product [filename]
  (let [commands (file->commands filename)
        {:keys [horizontal depth]} (reduce execute starting-location commands)]
    (* horizontal depth)))

(defn part-1 []
  (println
   "Day 2 - Part 1 - Location product: horizontal * depth:"
   (file->location-product "data/year_2021/day_2.input")))

(defn execute* [{:keys [horizontal depth aim] :as location} [instruction magnitude]]
  (cond
    (= instruction :forward) (-> (update location :horizontal #(+ % magnitude))
                                 (update :depth #(+ % (* magnitude aim))))
    (= instruction :down) (update location :aim #(+ % magnitude))
    (= instruction :up) (update location :aim #(- % magnitude))
    :else location))

(defn file->location-product* [filename]
  (let [commands (file->commands filename)
        {:keys [horizontal depth]} (reduce execute* starting-location commands)]
    (* horizontal depth)))

(defn part-2 []
  (println
   "Day 2 - Part 2 - Location product: horizontal * depth:"
   (file->location-product* "data/year_2021/day_2.input")))
