(ns net.bjoc.advent.year-2021.day-2
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))

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

;;;

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

;;;

(advent/defrunner 1 file->location-product "Horizontal * Depth")
(advent/defrunner 2 file->location-product* "Horizontal * Depth (modified)")
