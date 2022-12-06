(ns net.bjoc.advent.year-2022.day-6
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [zip]]))

(def packet-begin? #(= (count %) (count (set %))))

(defn index-of [pred coll]
  (keep-indexed (fn [i x] (when (pred x) i))
                coll))

(defn window [n chars]
  (apply zip (for [i (range n)]
               (drop i chars))))

(defn index-of-distinct [n chars]
  (->> chars
       (window n)
       (index-of packet-begin?)
       first
       (+ (dec n))))

(def index-of-packet (partial index-of-distinct 4))
(def index-of-packet* "alt version, 1-indexed" (comp inc index-of-packet))

(def file->packet-index* #(index-of-packet* (slurp %)))
  
;;;

(def index-of-message (partial index-of-distinct 14))
(def index-of-message* "alt version, 1-indexed" (comp inc index-of-message))

(def file->message-index* #(index-of-message* (slurp %)))

;;;

(advent/defrunner 1 file->packet-index* "1-Index of first packet")
(advent/defrunner 2 file->message-index* "1-Index of first message")
