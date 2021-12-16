(ns net.bjoc.advent.year-2021.day-16
  (:use [net.bjoc.advent.util.misc :only [take-until find-first]]
        [net.bjoc.advent.util.numeric :only [exp]])
  (:require [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.binary :as bin]))

(def hex-char->bin-char
  {\0 (list \0 \0 \0 \0)
   \1 (list \0 \0 \0 \1)
   \2 (list \0 \0 \1 \0)
   \3 (list \0 \0 \1 \1)
   \4 (list \0 \1 \0 \0)
   \5 (list \0 \1 \0 \1)
   \6 (list \0 \1 \1 \0)
   \7 (list \0 \1 \1 \1)
   \8 (list \1 \0 \0 \0)
   \9 (list \1 \0 \0 \1)
   \A (list \1 \0 \1 \0)
   \B (list \1 \0 \1 \1)
   \C (list \1 \1 \0 \0)
   \D (list \1 \1 \0 \1)
   \E (list \1 \1 \1 \0)
   \F (list \1 \1 \1 \1)})

(defn str->bits [s]
  (mapcat hex-char->bin-char s))

(defn file->bits [filename]
  (->> filename
       slurp
       str->bits))

(defn bits->int [bits]
  (->> bits (apply str) bin/read-binary-string))

(defn int->type [n]
  (case n
    0 :sum
    1 :product
    2 :minimum
    3 :maximum
    4 :literal
    5 :greater-than
    6 :less-than
    7 :equal-to))

(defn literal? [{:keys [type] :as packet}]
  (= type :literal))

(defn bits->literal [bits]
  (->> bits
       (partition 5)
       (take-until #(= (first %) \0))
       (mapcat #(drop 1 %))
       bits->int))

(def literal-size-caps (->> (range 1 16)
                            (map (partial exp 16))
                            (map dec)
                            (map vector (range 5 100 5))))

(defn packet-size [{:keys [type value lti subpackets] :as packet}]
  (+ 6
     (if (literal? packet)
       (->> (find-first #(<= value (second %)) literal-size-caps)
            first)
       (reduce + (case lti :total-length 16 :num-subpackets 12)
               (map packet-size subpackets)))))

(def length-type-id
  {\0 :total-length
   \1 :num-subpackets})

(def id-pool (atom 0))
(defn new-id! []
  (swap! id-pool inc))

(declare tl-subpackets ns-subpackets)

(defn packet [bits]
  (def thang bits)
  (let [p {:id (new-id!)
           :version (->> bits (take 3) bits->int)
           :type (->> bits (drop 3) (take 3) bits->int int->type)}
        bits (drop 6 bits)]
    (if (literal? p)
      (assoc p :value (bits->literal bits))
      (let [lti (length-type-id (first bits))
            bits (drop 1 bits)]
        (-> p
            (assoc :lti lti)
            (assoc :subpackets
                   (case lti
                     :total-length (tl-subpackets bits)
                     :num-subpackets (ns-subpackets bits))))))))

(defn tl-subpackets [bits]
  (let [size (->> bits
                  (take 15)
                  (apply str)
                  (bin/read-binary-string))]
    (loop [bits (->> bits
                     (drop 15)
                     (take size))
           subpackets [(packet bits)]]
      (let [new-bits (drop (packet-size (last subpackets)) bits)]
        (if (empty? new-bits)
          subpackets
          (recur new-bits
                 (conj subpackets (packet new-bits))))))))

(defn ns-subpackets [bits]
  (let [num (->> bits
                 (take 11)
                 (apply str)
                 (bin/read-binary-string))]
    (loop [bits (->> bits
                     (drop 11))
           subpackets [(packet bits)]]
      (let [new-bits (drop (packet-size (last subpackets)) bits)]
        (if (= (count subpackets) num)
          subpackets
          (recur new-bits
                 (conj subpackets (packet new-bits))))))))

(defn version-sum [{:keys [version subpackets] :as packet}]
  (reduce + version (map version-sum subpackets)))

(defn file->version-sum [filename]
  (->> filename file->bits packet version-sum))

;;;

(def operator-fn
  {:sum #(reduce + %)
   :product #(reduce * %)
   :minimum #(apply min %)
   :maximum #(apply max %)
   :greater-than #(if (apply > %) 1 0)
   :less-than #(if (apply < %) 1 0)
   :equal-to #(if (apply = %) 1 0)})

(defn eval-packet [{:keys [type value subpackets] :as packet}]
  (if (literal? packet)
    value
    (->> subpackets
         (map eval-packet)
         ((operator-fn type)))))

(defn file->evaluation [filename]
  (->> filename file->bits packet eval-packet))

;;;

(advent/defrunner 1 file->version-sum "Version sum of packet")
(advent/defrunner 2 file->evaluation "Result of packet evaluation")
