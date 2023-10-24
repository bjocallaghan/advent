(ns net.bjoc.advent.year-2016.day-7
  (:require [clojure.string :as str]
            [clojure.set :refer [intersection]]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [zip file->lines]]))

(defn line->nets [line]
  (let [[out in] (->> (str line "[]x")
                      (#(str/split % #"[\[\]]"))
                      (partition 2)
                      (apply zip))]
    {:supernets (remove empty? out)
     :hypernets (remove empty? in)}))

(defn subs-seq
  ([n s] (subs-seq n s 0))
  ([n s i]
   (when (>= (count s) n)
     (lazy-seq (cons (subs s i (+ i n))
                     (when (< i (- (count s) n))
                       (subs-seq n s (inc i))))))))

(defn abba? [[a b c d]]
  (and (= a d)
       (= b c)
       (not= a b)))

(defn tls? [line]
  (let [{:keys [supernets hypernets]} (line->nets line)
        supernet-quartets (mapcat (partial subs-seq 4) supernets)
        hypernet-quartets (mapcat (partial subs-seq 4) hypernets)]
    (and (some abba? supernet-quartets)
         (not (some abba? hypernet-quartets)))))

(defn num-ips [filter-pred filename]
  (->> filename
       file->lines
       (filter filter-pred)
       count))

(def num-tls-ips (partial num-ips tls?))

;;;

(def subs-3-seq (partial subs-seq 3))

(defn aba? [[a b c]]
  (and (= a c)
       (not= a b)))

(defn aba->bab [[a b _]]
  (str/join [b a b]))

(defn ssl? [line]
  (let [{:keys [supernets hypernets]} (line->nets line)
        supernet-aba-triplets (->> supernets
                                   (mapcat (partial subs-seq 3))
                                   (filter aba?)
                                   set)
        hypernet-bab-triplets (->> hypernets
                                   (mapcat (partial subs-seq 3))
                                   (filter aba?)
                                   (map aba->bab)
                                   set)]
    (not-empty (intersection supernet-aba-triplets hypernet-bab-triplets))))

(def num-ssl-ips (partial num-ips ssl?))

;;;

(advent/defrunner 1 num-tls-ips "Number of IPs supporting TLS")
(advent/defrunner 2 num-ssl-ips "Number of IPs supporting SSL")
