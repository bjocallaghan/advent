(ns net.bjoc.advent.year-2016.day-5
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.crypto :refer [md5]]
            [taoensso.timbre :as log]))

(def interesting? #(= (subs % 0 5) "00000"))

(defn find-next-offset [seed offset]
  (loop [offset offset]
    (let [hash (-> seed (str offset) md5)]
      (if (interesting? hash)
        (let [result {:hash hash
                      :offset offset}]
          (log/debug "Found an interesting hash:" result)
          result)
        (recur (inc offset))))))

(defn decrypt-seq
  ([seed] (decrypt-seq seed 0))
  ([seed offset]
   (let [result (find-next-offset seed offset)]
     (lazy-seq (cons result (decrypt-seq seed (-> result :offset inc)))))))

(defn seed->password [seed]
  (->> seed
       decrypt-seq
       (take 8)
       (map #(-> % :hash (nth 5)))
       str/join))

(def file->password #(-> % slurp str/trim seed->password))

;;;

(def size 8)

(defn done? [chars]
  (empty? (clojure.set/difference (-> size range set) (-> chars keys set))))

(def try-parse-long #(try (-> % str parse-long) (catch Exception _)))

(defn seed->password-2 [seed]
  (loop [chars {}
         decrypts (decrypt-seq seed)]
    (if (done? chars)
      (->> (range size)
           (map chars)
           str/join)
      (let [{hash :hash} (first decrypts)
            place (-> hash (nth 5) try-parse-long)
            c (nth hash 6)]
        (recur (merge {place c} chars)
               (rest decrypts))))))

(def file->password-2 #(-> % slurp str/trim seed->password-2))

;;;

(advent/defrunner 1 file->password "Password")
(advent/defrunner 2 file->password-2 "Password")
