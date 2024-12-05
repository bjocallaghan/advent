(ns net.bjoc.advent.year-2024.day-5
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(defn line->update [s]
  (map parse-long (str/split s #",")))

(defn line->rule* [s]
  (map parse-long (str/split s #"\|")))

(defn rule*->rule [[a b]]
  (fn [update]
    ;; filter the pages to look for only pages a and b
    (let [[c d] (filter #{a b} update)]
      ;; if both pages are found
      (if d
        ;; check to ensure that the first page found (c) is the first expected page (a)
        (= a c)
        ;; if both pages aren't present, it automatically passes
        true))))

(def line->rule (comp rule*->rule line->rule*))

(def middle #(nth % (-> (count %) dec (/ 2))))

(defn file->middle-sum [filename]
  (let [lines (file->lines filename)
        master-rule (apply every-pred
                           (map line->rule
                                (take-while not-empty lines)))
        updates (map line->update
                     (drop 1 (drop-while not-empty lines)))]
    (->> updates
         (filter master-rule)
         (map middle)
         (reduce +))))

;;;

(defn improve-update [v rules*]
  (let [[a b] (first (filter (fn [[a b]]
                               (let [[c d] (filter #{a b} v)]
                                 (and (= a d) (= b c))))
                             rules*))
        a-idx (.indexOf v a)
        b-idx (.indexOf v b)]
    (vec (concat (subvec v 0 b-idx)
                 (subvec v (inc b-idx) (inc a-idx))
                 [b]
                 (subvec v (inc a-idx))))))

(defn fix-update
  ([update rules*] (fix-update update rules* (apply every-pred (map rule*->rule rules*))))
  ([update rules* master-rule]
  (if (master-rule update)
    update
    (fix-update (improve-update update rules*) rules* master-rule))))

(defn file->middle-sum* [filename]
  (let [lines (->> filename
                   file->lines)
        rules* (map line->rule* (take-while not-empty lines))
        master-rule (apply every-pred
                           (map line->rule
                                (take-while not-empty lines)))
        updates (map line->update
                     (drop 1 (drop-while not-empty lines)))]
    (->> updates
         (remove master-rule)
         (map vec)
         (map #(fix-update % rules*))
         (map middle)
         (reduce +))))

;;;

(advent/defrunner 1 file->middle-sum "Sum of middle pages of valid updates")
(advent/defrunner 2 file->middle-sum* "Sum of middle pages of corrected updates")
