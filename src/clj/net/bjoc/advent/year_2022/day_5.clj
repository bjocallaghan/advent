(ns net.bjoc.advent.year-2022.day-5
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(defn apply-row [stacks row]
  (let [row-nums (range 1 (-> row count inc))]
    (reduce (fn [stacks [k crate]]
              (if (not= crate \space)
                (update stacks k conj crate)
                stacks))
            stacks (map vector row-nums row))))

(defn lines->stacks [lines]
  (let [rows (->> lines
                  reverse
                  (drop 1)
                  (map #(str % " "))
                  (map (fn [line]
                         (->> line
                              (partition 4)
                              (mapv second)))))
        row-nums (range 1 (-> rows first count inc))
        stacks (into {} (map vector row-nums (repeat '())))]
    (reduce apply-row stacks rows)))

(defn line->instruction [line]
  (let [[a b c] (->> (re-find #"^move (\d+) from (\d+) to (\d+)$" line)
                     rest
                     (map #(Integer/parseInt %)))]
    {:src b :dest c :quantity a}))

(defn file->state [filename]
  (let [lines (file->lines filename)]
    {:stacks (lines->stacks (->> lines
                                 (take-while not-empty)))
     :instructions (->> lines
                        (drop-while not-empty)
                        (drop 1)
                        (map line->instruction))}))

(defn apply-instruction [move-multiple? stacks {:keys [src dest quantity]}]
  (let [extra-op (if move-multiple? identity reverse)]
    (-> stacks
        (update src #(drop quantity %))
        (update dest (comp flatten conj) (->> (stacks src) (take quantity) extra-op)))))

(defn- file->top-crates* [move-multiple? filename]
  (let [{:keys [stacks instructions]} (file->state filename)]
    (->> (reduce (partial apply-instruction move-multiple?) stacks instructions)
         sort
         vals
         (map first)
         (apply str))))

(def file->top-crates (partial file->top-crates* false))

;;;

(def file->top-crates-2 (partial file->top-crates* true))

;;;

(advent/defrunner 1 file->top-crates "Top crates (model 9000)")
(advent/defrunner 2 file->top-crates-2 "Top crates (model 9001)")
