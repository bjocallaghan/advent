(ns net.bjoc.advent.year-2022.day-2
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))

(def hierarchy [:rock :paper :scissors])

(defn outcome [[theirs mine]]
  (let [best-move (second (drop-while #(not= % theirs) (cycle hierarchy)))]
    (cond
      (= mine best-move) :win
      (= mine theirs) :draw
      :else :lose)))

(defn line->round [line]
  (->> (str/split line #"\s")
       (map {"A" :rock
             "B" :paper
             "C" :scissors
             "X" :rock
             "Y" :paper
             "Z" :scissors})))

(def score
 {:rock 1
  :paper 2
  :scissors 3
  :win 6
  :draw 3
  :lose 0})

(defn round->score [round]
  (let [[theirs mine] round]
    (+ (score mine)
       (score (outcome round)))))

(defn- file->score* [line->round-fn filename]
  (->> (slurp filename)
       (#(str/split % #"\n"))
       (map line->round-fn)
       (map round->score)
       (reduce +)))

(def file->score (partial file->score* line->round))

;;;

(def find-first #(first (filter %1 %2)))

(defn line->round-2 [line]
  (let [[theirs target-outcome] (->> (str/split line #"\s")
                                     (map {"A" :rock
                                           "B" :paper
                                           "C" :scissors
                                           "X" :lose
                                           "Y" :draw
                                           "Z" :win}))
        mine (find-first #(= target-outcome (outcome [theirs %])) hierarchy)]
    [theirs mine]))

(def file->score-2 (partial file->score* line->round-2))

;;;

(advent/defrunner 1 file->score "Score (strat guide 1)")
(advent/defrunner 2 file->score-2 "Score (strat guide 2)")
