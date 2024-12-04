(ns net.bjoc.advent.year-2024.day-3
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [zip]]))

(def mul-pattern #"(mul)\((\d+),(\d+)\)")

(defn mul-sum [s]
  (->> (re-seq mul-pattern s)
       (map rest)
       (map rest)
       (map #(map parse-long %))
       (map #(apply * %))
       (reduce +)))

(def file->mul-sum (comp mul-sum slurp))

;;;

(def do-pattern #"(do(?:n't)?)\(\)")

(defn or-patterns [& patterns]
  (->> patterns
       (map #(str "(?:" % ")"))
       (str/join "|")
       re-pattern))

(defn parse [s]
  (let [pattern (or-patterns mul-pattern do-pattern)]
    (->> (re-seq pattern s)
         (map #(remove nil? %))
         (map rest)
         (map (fn [[op & operands]]
                (let [rtn {:op (keyword op)}]
                (if operands
                  (assoc rtn :operands (map parse-long operands))
                  rtn)))))))

(defn eval-op [{:keys [acc on?] :as state} {:keys [op operands] :as op}]
  (case op
    :do (assoc state :on? true)
    :don't (assoc state :on? false)
    :mul (if on?
           (update state :acc + (apply * operands))
           state)))

(defn mul-sum* [s]
  (:acc (reduce eval-op
                {:acc 0 :on? true}
                (parse s))))

(def file->mul-sum* (comp mul-sum* slurp))

;;;

(advent/defrunner 1 file->mul-sum "Sum of mul ops")
(advent/defrunner 2 file->mul-sum* "Sum of [on] mul ops")
