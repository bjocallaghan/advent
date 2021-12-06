(ns net.bjoc.advent.year-2015.day-11
  (:require [clojure.string :as str])
  (:use [net.bjoc.advent.util.misc :only [find-first]]
        [net.bjoc.advent.util.numeric :only [exp]]))

(def places
  "Place values, base 26"
  (->> (range 8)
       reverse
       (map (partial exp 26))))

(defn char->number [c]
  (if (= c \space)
    0
    (- (int c) 97)))

(defn number->char [n]
  (char (+ 97 n)))

(defn password->number [password]
  (let [s (format "%8s" password)]
    (->> (map char->number s)
         (map vector places)
         (reduce (fn [n [a b]] (+ n (* a b))) 0))))

(defn number->password [n]
  (let [raw (loop [n n
                   chars (list)]
              (if (zero? n)
                (str/join chars)
                (recur (quot n 26)
                       (conj chars (number->char (mod n 26))))))]
    (str (str/join (repeat (- 8 (count raw)) \a)) raw)))

(defn valid-req-1? [password]
  (let [ascending? (fn [[a b c]] (= (+ (char->number a) 2)
                                    (+ (char->number b) 1)
                                    (+ (char->number c) 0)))]
    (some ascending? (map vector password (drop 1 password) (drop 2 password)))))

(defn valid-req-2? [password]
  (let [contents (set password)]
    (not (or (contents \i) (contents \o) (contents \l)))))

(defn valid-req-3? [password]
  (let [pair? (fn [[a b]] (= a b))]
    (->> (map vector password (drop 1 password))
         (filter pair?)
         (into #{})
         count
         (<= 2))))

(def valid? (every-pred valid-req-1? valid-req-2? valid-req-3?))

(defn generate [s]
  (loop [n (inc (password->number s))]
    (if (-> n number->password valid?)
      (-> n number->password)
      (recur (inc n)))))

(defn file->generate [filename]
  (generate (slurp filename)))

(defn part-1 []
  (println
   "Day 11 - Part 1 - Next password:"
   (file->generate "data/year_2015/day_11.input")))

(defn file->generate* [filename]
  (-> filename slurp generate generate))

(defn part-2 []
  (println
   "Day 11 - Part 2 - Next next password:"
   (file->generate* "data/year_2015/day_11.input")))
