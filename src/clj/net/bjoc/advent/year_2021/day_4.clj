(ns net.bjoc.advent.year-2021.day-4
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.numeric :as n]
            [net.bjoc.advent.util.matrix :as mtx]))

(defn file->draws [filename]
  (map n/parse-int (-> (slurp filename)
                       (str/split #"\n")
                       first
                       (str/split #","))))

(defn matrix->card [matrix]
  (reduce-kv (fn [m k v]
               (assoc m k {:value v :status :unmarked}))
             {} matrix))

(defn file->cards [filename]
  (->> (-> (slurp filename)
           (str/split #"\n\n")
           rest)
       (map mtx/from-string)
       (map matrix->card)))

(defn mark [card x & more-xs]
  (let [xs (into #{} (conj more-xs x))]
    (reduce-kv (fn [m k {:keys [value] :as v}]
                 (assoc m k (if (xs value) (assoc v :status :marked) v)))
               {} card)))

(defn score [card]
  (->> (vals card)
       (filter #(= (:status %) :unmarked))
       (map :value)
       (reduce +)))

(defn all-marked? [cell-values]
  (every? #(= :marked %) (map :status cell-values)))

(defn bingo? [card]
  (when (->> (concat (mtx/rows card) (mtx/cols card))
             (some all-marked?))
    card))

(defn game [draws cards]
  (let [draw (first draws)
        marked-cards (map #(mark % draw) cards)]
    (if-let [card (some bingo? marked-cards)]
      {:winning-card card
       :winning-draw draw}
      (if (empty? (rest draws))
        {:winning-card :none
         :winning-draw :none}
        (recur (rest draws) marked-cards)))))

(defn file->game-score [filename]
  (let [draws (file->draws filename)
        cards (file->cards filename)
        {:keys [winning-card winning-draw]} (game draws cards)]
    (* (score winning-card) winning-draw)))

;;;

(defn bad-game [draws cards]
  (let [draw (first draws)
        marked-cards (map #(mark % draw) cards)]
    (if (and (= 1 (count marked-cards))
             (bingo? (first marked-cards)))
      {:winning-card (first marked-cards)
       :winning-draw draw}
      (recur (rest draws) (remove bingo? marked-cards)))))

(defn file->bad-game-score [filename]
  (let [draws (file->draws filename)
        cards (file->cards filename)
        {:keys [winning-card winning-draw]} (bad-game draws cards)]
    (* (score winning-card) winning-draw)))

;;;

(advent/defrunner 1 file->game-score "Winning score * final draw")
(advent/defrunner 2 file->bad-game-score "Winning score * final draw (bad)")
