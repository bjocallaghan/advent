(ns net.bjoc.advent.year-2022.day-11
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(defn make-handle-fn [line]
  (let [[arg1 op arg2] (-> line
                           (str/split #" = ")
                           second
                           (str/split #"\s"))]
    ;; I'm a failure and a con-artist. I thought I could do this with macros and
    ;; parsing, ultimately creating a function on the fly for arbitrary
    ;; operations and more flexible input. But I couldn't figure out how to do
    ;; it. *sob*
    ;;
    ;; Below is hard-coded / semi-arbitrary; suitable only for the today's
    ;; example and my provided input.
    (cond
      (= arg2 "old") #(* % %)
      (= op "+") #(+ % (Integer/parseInt arg2))
      (= op "*") #(* % (Integer/parseInt arg2)))))

(defn make-target-fn [lines]
  (let [[a b c] (map #(Integer/parseInt %) (re-seq #"\d+" (str/join lines)))]
    (fn [x] (if (zero? (mod x a)) b c))))

(defn lines->monkey [lines]
  {:items (mapv #(Integer/parseInt %) (re-seq #"\d+" (nth lines 1)))
   :inspections 0
   :handle-fn (make-handle-fn (nth lines 2))
   :target-fn (make-target-fn (drop 3 lines))})

(defn file->monkeys [filename]
  (->> filename
       file->lines
       (partition-by empty?)
       (remove #(= (count %) 1))
       (mapv lines->monkey)))

(defn step-item [{:keys [monkeys de-worry-fn] :as state} n]
  (let [item (get-in monkeys [n :items 0])
        new-item (-> ((get-in monkeys [n :handle-fn]) item) de-worry-fn)
        target ((get-in monkeys [n :target-fn]) new-item)]
    (-> state
        (update-in [:monkeys n :inspections] inc)
        (update-in [:monkeys n :items] subvec 1)
        (update-in [:monkeys target :items] conj new-item))))

(defn step-monkey [{:keys [monkeys] :as state} n]
  (loop [state state]
    (if (empty? (get-in state [:monkeys n :items]))
      state
      (recur (step-item state n)))))

(defn round [state _]
  (reduce step-monkey state (-> state :monkeys count range)))

(defn file->monkey-business [de-worry-fn num-rounds filename]
  (let [state {:monkeys (file->monkeys filename)
               :de-worry-fn de-worry-fn}]
    (->> (reduce round state (range num-rounds))
         :monkeys
         (map :inspections)
         sort
         reverse
         (take 2)
         (reduce *))))

(defn file->monkey-business-1 [filename]
  (file->monkey-business #(quot % 3) 20 filename))

;;;

(defn file->monkey-business-2 [filename]
  (let [divisors (->> filename
                      file->lines
                      (keep #(second (re-find #"divisible by (\d+)$" %)))
                      (map #(Integer/parseInt %)))
        de-worry-fn #(mod % (reduce * divisors))]
    (file->monkey-business de-worry-fn 10000 filename)))

;;;

(advent/defrunner 1 file->monkey-business-1 "Monkey-business")
(advent/defrunner 2 file->monkey-business-2 "Monkey-business")
