(ns net.bjoc.advent.year-2023.day-5
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(defn line->fn [line]
  (let [[dest-start src-start len] (->> (str/split line #"\s+")
                                        (map parse-long))]
    ;; works, but is memory intensive
    (into {} (map vector
                  (range src-start (+ src-start len))
                  (range dest-start (+ dest-start len))))))

(defn spec->fn [lines]
  (let [fns (->> lines
                 (drop 1)
                 (map line->fn))]
    (fn [x]
      (let [result (or (->> fns
                            (map #(% x))
                            (remove nil?)
                            first)
                       x)]
        ;; (println x "->" result)
        result))))

(defn file->xform [filename]
  (->> (str/split (slurp filename) #"\n\n")
       (drop 1)
       (map #(str/split % #"\n"))
       (map spec->fn)
       reverse
       (apply comp)))

(defn file->best-location [filename]
  (let [xform (file->xform filename)
        seeds (->> (str/split (slurp filename) #"\n")
                   first
                   (#(str/split % #"\s+"))
                   (map parse-long)
                   (remove nil?))]
    (apply min (map xform seeds))))
  
;;;

;;;

(advent/defrunner 1 file->best-location "Best location")
(advent/defrunner 2 identity "")
