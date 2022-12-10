(ns net.bjoc.advent.year-2022.day-10
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]
            [net.bjoc.advent.util.matrix-visualization :as vis]
            ))

(def starting-state {:x 1 :clock 1 :screen {}})

(defn file->instructions [filename]
  (->> (file->lines filename)
       (map #(str/split % #"\s+"))
       (mapcat (fn [[op arg]]
                 (case op
                   "noop" [{:op :noop}]
                   "addx" [{:op :noop} {:op :addx :arg (Integer/parseInt arg)}])))))

(defn clock->pos [clock] [(mod clock 40) (quot clock 40)])

(defn draw [screen x clock]
  (let [sprite? (->> 3 range (map #(+ (- x 1) %)) set)]
    (if (sprite? (mod clock 40))
      (conj screen [(clock->pos clock) \x])
      screen)))

(defn step [{:keys [x clock] :as state} {:keys [op arg] :as instructions}]
  (-> state
      (update :screen draw x (dec clock))
      (update :x + (if (= :addx op) arg 0))
      (update :clock inc)))

(defn signal-at [clock-num instructions]
  (let [step* (comp #(if (= clock-num (:clock %)) (reduced %) %) step)
        {:keys [x clock]} (reduce step* starting-state instructions)]
    (* x clock)))

(defn signal-sum [filename]
  (let [instructions (file->instructions filename)]
    (->> (map #(+ 20 (* 40 %)) (range 6))
         (map #(signal-at % instructions))
         (reduce +))))

;;;

(defn write-screen [filename]
  (let [dest "visualizations/2022_day_10.png"]
    (vis/write-image-file (->> (file->instructions filename)
                               (reduce step starting-state)
                               :screen)
                          dest)
    dest))

;;;

(advent/defrunner 1 signal-sum "Sum of signal strengths")
(advent/defrunner 2 write-screen "Wrote image file")
