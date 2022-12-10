(ns net.bjoc.advent.year-2022.day-9
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.coords :refer [add-coords difference]]
            [net.bjoc.advent.util.misc :refer [zip file->lines]]))

(def starting-state {:head [0 0] :tail [0 0] :visited #{}})

(defn line->moves [line]
  (let [[dir raw-n] (rest (re-find #"^(.) (\d+)$" line))]
    (repeat (Integer/parseInt raw-n) ({"R" [1 0]
                                       "L" [-1 0]
                                       "U" [0 1]
                                       "D" [0 -1]} dir))))

(defn file->moves [filename]
  (->> filename
       file->lines
       (mapcat line->moves)))

(defn make-knots [size]
  (vec (repeat size {:xy [0 0] :prev-xy [0 0] :visited #{}})))

(defn smash [xy]
  (mapv #(cond (zero? %) 0 (pos? %) 1 (neg? %) -1) xy))

(defn move-tail [tail new-head]
  (let [diff (difference new-head tail)]
    (if (some #(>= (Math/abs %) 2) diff)
      (add-coords (smash diff) tail)
      tail)))

(defn step [knots dir]
  (reduce (fn [knots n]
            (let [tail (:xy (nth knots n))
                  {new-head :xy} (nth knots (dec n))]
              (-> knots
                  (update-in [n :xy] move-tail new-head)
                  (update-in [n :visited] conj tail))))
          (assoc-in knots [0 :xy] (add-coords (get-in knots [0 :xy]) dir))
          (range 1 (count knots))))

(defn file->visited-count [n filename]
  (let [final-knots (reduce step (make-knots n) (file->moves filename))
        {:keys [xy visited] :as last-knot} (last final-knots)]
    (count (conj visited xy))))

;;;

(advent/defrunner 1 (partial file->visited-count 2) "Count of visited coords (2-knots)")
(advent/defrunner 2 (partial file->visited-count 10) "Count of visited coords (10-knots)")
