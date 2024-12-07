(ns net.bjoc.advent.year-2024.day-7
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines zip]]))

(defn create-op-groups [n]
  (loop [n (dec n)
         acc (list (list +) (list *))]
    (if (<= n 0)
      acc
      (recur (dec n)
             (concat (map #(conj % +) acc)
                     (map #(conj % *) acc))))))

(defn works? [target & operands]
  (let [apply-op (fn [acc [f x]]
                   (let [res (f acc x)]
                     (if (> res target)
                       (reduced res)
                       res)))]
  (->> (for [op-group (create-op-groups (-> operands count dec))]
         (reduce apply-op (first operands) (zip op-group (rest operands))))
       (filter #(= target %))
       first)))

(defn file->good-sum [filename]
  (->> filename
       file->lines
       (map (fn [s]
              (map parse-long 
                   (str/split s #":?\s+")
                   )))
       (map #(apply works? %))
       (remove nil?)
       (reduce +)))
    
;;;
  
(def || (comp parse-long str))

(defn create-op-groups* [n]
  (loop [n (dec n)
         acc (list (list +) (list *) (list ||))]
    (if (<= n 0)
      acc
      (recur (dec n)
             (concat (map #(conj % +) acc)
                     (map #(conj % *) acc)
                     (map #(conj % ||) acc))))))

(defn works?* [target & operands]
  (let [apply-op (fn [acc [f x]]
                   (let [res (f acc x)]
                     (if (> res target)
                       (reduced res)
                       res)))]
  (->> (for [op-group (create-op-groups* (-> operands count dec))]
         (reduce apply-op (first operands) (zip op-group (rest operands))))
       (filter #(= target %))
       first)))

(defn file->good-sum* [filename]
  (->> filename
       file->lines
       (map (fn [s]
              (map parse-long 
                   (str/split s #":?\s+")
                   )))
       (map #(apply works?* %))
       (remove nil?)
       (reduce +)))

;;;

(advent/defrunner 1 file->good-sum "Sum of good targets")
(advent/defrunner 2 file->good-sum* "Sum of good targets (revised)")
