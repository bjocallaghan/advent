(ns net.bjoc.advent.year-2020.day-22
  (:require [clojure.string :as str]))

(defonce verbose false)

(defn vprintln [& xs]
  (when verbose
    (apply println xs)))

(defn card-file->hands [filename]
  (into {} (map vector
                [:p1 :p2]
                (->> (slurp filename)
                     (#(str/split % #"\n\n"))
                     (map (fn [s]
                            (->> (str/replace s #"Player \d:\n" "")
                                 (#(str/split % #"\s+"))
                                 (map #(Integer/parseInt %))
                                 vec)))))))

(defn round [hands rnd-num]
  (let [winner (first (last (sort-by #(first (second %)) hands)))
        played-cards (->> (map #(-> % second first) hands)
                          sort
                          reverse)
        hands (into {} (for [[k v] hands] [k (subvec v 1)]))]
    (vprintln "-- Round" rnd-num "--")
    (vprintln "Player 1's deck:" (:p1 hands))
    (vprintln "Player 2's deck:" (:p2 hands))
    (vprintln "Player 1 plays:" (-> hands :p1 first))
    (vprintln "Player 2 plays:" (-> hands :p2 first))
    (vprintln "Player" (str/replace (str :p2) #":p" "") "wins the round!")
    (vprintln)
    (update hands winner (partial reduce conj) played-cards)))
        
(defn game [hands & {:keys [rules rnd-num] :or {rules round rnd-num 1}}]
   (if (some empty? (vals hands))
     (do
       (vprintln)
       (vprintln "== Post-game results ==")
       (vprintln "Player 1's deck:" (:p1 hands))
       (vprintln "Player 2's deck:" (:p2 hands))
       (vprintln)
       hands)
     (recur (rules hands rnd-num) {:rules rules :rnd-num (inc rnd-num)})))

(defn score [hand]
  (->> (map * (reverse hand) (range 1 (-> hand count inc)))
       (reduce +)))

(defn file->winner-score [filename]
  (apply max (for [[_ v] (game (card-file->hands filename))] (score v))))

(defn part-1 []
  (println
   "Day 22 - Part 1 - Score of Combat winner:"
   (file->winner-score "data/year_2020/day_22.input")))

(def game-num-sequence (atom 0))
(defn pull-game-num! []
  (vprintln "Pulling a game number from the sequence")
  (swap! game-num-sequence inc))

(def infinite-loop-protection (atom 0))
(defn init! []
  (reset! game-num-sequence 0)
  (reset! infinite-loop-protection 0))

(def p1? #(= % :p1))

(defn game*
  [{:keys [p1 p2 rnd-num game-num seen]
    :or {rnd-num 1
         seen #{}}
    :as hands}]
  (let [game-num (or game-num (pull-game-num!))]
    (vprintln "== Game" game-num "Round" rnd-num "==")
    (vprintln "Player 1:" p1)
    (vprintln "Player 2:" p2)
    (cond
      ;; loop detected, automatic player 1 victory
      (seen (dissoc hands :rnd-num :seen))
      (let []
        (vprintln "Game" game-num "automatic win for Player 1 due to loop detection.")
        {:winner :p1
         :cards :loop-detection-flag})
      ;; a player has run out of cards, game over
      (some empty? [p1 p2])
      (let [winner (if (empty? p1) :p2 :p1)]
        (vprintln "Game" game-num "over. Winner: Player"
                  (str/replace (name winner) #"p" ""))
        {:winner winner
         :cards (if (empty? p1) p2 p1)})
      ;; play the round
      :default
      (let [played (into {} (for [[p hand] hands :when (vector? hand)] [p (first hand)]))
            winner (cond
                     (and (> (count p1) (:p1 played))
                          (> (count p2) (:p2 played)))
                     (:winner (game* {:p1 (subvec p1 1 (inc (:p1 played)))
                                      :p2 (subvec p2 1 (inc (:p2 played)))}))
                     (> (:p1 played) (:p2 played)) :p1
                     :else :p2)
            result (-> hands
                       (update :p1 subvec 1)
                       (update :p2 subvec 1)
                       (update winner
                               (partial reduce conj)
                               (map played (if (p1? winner)
                                             [:p1 :p2]
                                             [:p2 :p1])))
                       (assoc :game-num game-num)
                       (assoc :rnd-num (+ 1 rnd-num))
                       (assoc :seen (conj seen (dissoc hands :rnd-num :seen))))]
        (vprintln "Round" rnd-num "(Game" game-num ") Winner: Player"
                  (str/replace (name winner) #"p" ""))
        (vprintln)
        (recur result)))))

(defn file->winner-score* [filename]
  (-> (card-file->hands filename)
      game*
      :cards
      score))

(defn part-2 []
  (println
   "Day 22 - Part 2 - Recursive combat winner score:"
   (file->winner-score* "data/year_2020/day_22.input")))
