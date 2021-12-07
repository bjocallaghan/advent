(ns net.bjoc.advent.year-2021.day-3
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.binary :as b]))

(defn file->diagnostics [filename]
  (str/split (slurp filename) #"\n"))

(defn diagnostics-frequencies [diagnostics]
  (let [len (-> diagnostics first count)]
    (->> (for [i (range len)] (map #(nth % i) diagnostics))
         (map frequencies))))

(defn gamma-rate [diagnostics]
  (->> (diagnostics-frequencies diagnostics)
       (map #(->> % keys (sort-by %)))
       (map last)
       (str/join)
       (b/read-binary-string)))

(defn epsilon-rate [diagnostics]
  (->> (diagnostics-frequencies diagnostics)
       (map #(->> % keys (sort-by %)))
       (map first)
       (str/join)
       (b/read-binary-string)))

(defn power-consumption [diagnostics]
  (* (gamma-rate diagnostics) (epsilon-rate diagnostics)))

(defn file->power-consumption [filename]
  (-> filename file->diagnostics power-consumption))

;;;

(defn- most-prevalent-in-nth-place [diagnostics n]
  (let [m (-> (diagnostics-frequencies diagnostics)
              (nth n))]
    (if (> (m \0) (m \1)) \0 \1)))

(defn oxygen-generator-rating [diagnostics]
  (let [len (-> diagnostics first count)]
    (loop [diagnostics diagnostics
           i 0]
      (if (= 1 (count diagnostics))
        (-> diagnostics first b/read-binary-string)
        (let [best (most-prevalent-in-nth-place diagnostics i)]
          (recur (filter #(= (nth % i) best)
                         diagnostics)
                 (mod (inc i) len)))))))

(defn- least-prevalent-in-nth-place [diagnostics n]
  (let [m (-> (diagnostics-frequencies diagnostics)
              (nth n))]
    (if (< (m \1) (m \0)) \1 \0)))

(defn co2-scrubber-rating [diagnostics]
  (let [len (-> diagnostics first count)]
    (loop [diagnostics diagnostics
           i 0]
      (if (= 1 (count diagnostics))
        (-> diagnostics first b/read-binary-string)
        (let [best (least-prevalent-in-nth-place diagnostics i)]
          (recur (filter #(= (nth % i) best)
                         diagnostics)
                 (mod (inc i) len)))))))

(defn life-support-rating [diagnostics]
  (* (oxygen-generator-rating diagnostics) (co2-scrubber-rating diagnostics)))

(defn file->life-support-rating [filename]
  (-> filename file->diagnostics life-support-rating))

;;;

(advent/defrunner 1 file->power-consumption "Power consumption")
(advent/defrunner 2 file->life-support-rating "Life support rating")
