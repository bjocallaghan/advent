(ns net.bjoc.advent.year-2020.day-25
  (:use [clojure.java.shell :only [sh]])
  (:require [clojure.string :as str]
            [taoensso.timbre :as timbre
             ;; :refer [log  trace  debug  info  warn  error  fatal  report
             ;;         logf tracef debugf infof warnf errorf fatalf reportf
             ;;         spy get-env]])
             :refer [trace debug info warn error fatal]]
            ))

(defn xform* [loop-size subject_num]
  ;; (println "transforming" loop-size subject_num)
  (loop [x 1
         loop-size loop-size]
    (if (pos? loop-size)
      (recur (-> x (* subject_num) (rem 20201227))
             (dec loop-size))
      x)))

(def xform (memoize xform*))

(comment
  (let [subject_num 7
        target 5764801]
    (->> (range 15)
         (map #(vector % (xform % subject_num)))
         (filter (fn [[_ result]] (= target result)))
         ))

  (let [subject_num 7
        targets #{17807724 5764801}]
    (->> (range 15)
         (map #(vector % (xform % subject_num)))
         (filter (fn [[_ result]] (targets result)))
         ))

  (let [subject_num 7
        targets #{17807724 5764801}
        discovered-loop-size  (->> (range 10000)
                                   (filter #(targets (xform % subject_num)))
                                   first)
        opposite-public-key (let [[x y] (vec targets)]
                              (if (= (xform discovered-loop-size subject_num) x) y x))
        ]
    (xform discovered-loop-size opposite-public-key))
  )

(defn crack
  ([pub-key-1 pub-key-2] (crack pub-key-1 pub-key-2 5))
  ([pub-key-1 pub-key-2 crack-limit]
   (let [subject_num 7
         targets #{pub-key-1 pub-key-2}
         discovered-loop-size  (->> (range crack-limit)
                                    (filter #(targets (xform % subject_num)))
                                    first)
         _ (when (nil? discovered-loop-size)
             (throw (ex-info "Crack failed. Loop size not discovered within search space."
                             {:crack-limit crack-limit})))
         opposite-public-key (let [[x y] (vec targets)]
                               (if (= (xform discovered-loop-size subject_num) x) y x))]
     (xform discovered-loop-size opposite-public-key))))

(comment
  (crack 2959251 4542595 1000000)
  )

;; (defn prime? [n]
;;   (let [limit (->> n
;;                    (java.lang.Math/sqrt)
;;                    str
;;                    (re-find #"^(\d+)")
;;                    second
;;                    Integer/parseInt
;;                    inc)]
;;     (loop 



(defn part-1 []
  (println
   "Day 22 - Part 1 - asdf asdf:"
   ))

(defn part-2 []
  (println
   "Day 22 - Part 2 - asdf asdf:"
   ))
