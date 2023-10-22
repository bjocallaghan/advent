(ns net.bjoc.advent.year-2016.day-2
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.coords :refer [add-coords]]
            [net.bjoc.advent.util.matrix :as matrix]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(def keypad-1
  (matrix/from-string
   "1 2 3
    4 5 6
    7 8 9"))

(defn in-bounds-fn [keypad] (-> keypad keys set))

(def initial-state-1 {:position [1 1] :output "" :keypad keypad-1})

(def instructions
  {\U [ 0 -1]
   \D [ 0  1]
   \L [-1  0]
   \R [ 1  0]})

(def line->instructions #(map instructions %))

(defn apply-instruction [keypad position instruction]
  (let [in-bounds? (in-bounds-fn keypad)]
    (if-let [result (in-bounds? (add-coords position instruction))]
      result
      position)))

(defn apply-instructions [{:keys [position output keypad] :as state} instructions]
  (let [new-position (reduce (partial apply-instruction keypad) position instructions)]
    (-> state
        (assoc :position new-position)
        (update :output str (keypad new-position)))))

(defn file->code* [initial-state filename]
  (->> (file->lines filename)
       (map line->instructions)
       (reduce apply-instructions initial-state)
       :output
       ))

(def file->code (partial file->code* initial-state-1))

;;;

(def keypad-2
  {[2 0] 1
   [1 1] 2
   [2 1] 3
   [3 1] 4
   [0 2] 5
   [1 2] 6
   [2 2] 7
   [3 2] 8
   [4 2] 9
   [1 3] \A
   [2 3] \B
   [3 3] \C
   [2 4] \D})

(def initial-state-2 {:position [0 2] :output "" :keypad keypad-2})

(def file->code-2 (partial file->code* initial-state-2))

;;;

(advent/defrunner 1 file->code "Code with normal")
(advent/defrunner 2 file->code-2 "Code with crazy keypad")
