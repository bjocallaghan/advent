(ns net.bjoc.advent.year-2023.day-15
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [zip]]))

(defn hash* [s]
  (reduce (fn [acc c]
            (-> acc (+ (int c)) (* 17) (mod 256)))
          0 s))

(defn file-hash [filename]
  (->> filename
       slurp
       (#(str/replace % #"\n" ""))
       (#(str/split % #","))
       (map hash*)
       (reduce +)))

;;;

(defn parse-instruction [s]
  (let [[_ a b c] (re-find #"(^[a-z]+)([^a-z])(\d?)$" s)]
    (merge (if (= b "=")
             {:operation :upsert
              :label a
              :size (parse-long c)}
             {:operation :remove
              :label a})
           {:label a
            :target (hash* a)})))

(def keepv (comp vec keep))

(defn apply-instruction [boxes {:keys [operation target label size]}]
  (let [target-box (get boxes target [])
        target-box-labels (set (map first target-box))
        next-box (if (= operation :upsert)
                   (if (target-box-labels label)
                     (keepv (fn [[label2 size2]]
                              (if (= label label2) 
                                [label size]
                                [label2 size2]))
                            target-box)
                     (conj target-box [label size]))
                   (filterv (fn [[label2 _]] (not= label label2)) target-box))]
    (assoc boxes target next-box)))

(defn file->focusing-power [filename]
  (->> filename
       slurp
       (#(str/replace % #"\n" ""))
       (#(str/split % #","))
       (map parse-instruction)
       (reduce apply-instruction {})
       (mapcat (fn [[box-number lenses]]
                 (map-indexed (fn [i [_ size]]
                                {:box-number box-number
                                 :slot-number (inc i)
                                 :size size})
                              lenses)))
       (map (fn [{:keys [box-number slot-number size]}]
              (* (inc box-number) slot-number size)))
       (reduce +)))

;;;

(advent/defrunner 1 file-hash "Sum of hashes")
(advent/defrunner 2 file->focusing-power "Focusing power")
