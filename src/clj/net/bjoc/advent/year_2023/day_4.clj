(ns net.bjoc.advent.year-2023.day-4
  (:require [clojure.string :as str]
            [clojure.set :refer [intersection]]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines]]))

(defn line->card [line]
  (let [[_ id a b] (re-find #"^Card\s+(\d+):\s+(.+)\s+\|\s+(.+)\s*$" line)]
    {:line line
     :id (parse-long id)
     :winning-numbers (->> (str/split a #"\s+") (map parse-long) set)
     :numbers-you-have (->> (str/split b #"\s+") (map parse-long) set)}))

(defn assoc-num-matches [{:keys [winning-numbers numbers-you-have] :as card}]
  (assoc card :num-matches
         (count (intersection winning-numbers numbers-you-have))))

(defn assoc-score [{:keys [num-matches] :as card}]
  (assoc card :score
         (long (Math/pow 2 (dec num-matches)))))

(defn file->score-sum [filename]
  (->> filename
       file->lines
       (map line->card)
       (map assoc-num-matches)
       (map assoc-score)
       (map :score)
       (reduce +)))

;;;

(defn file->card-total [filename]
  (let [scores-by-id (->> filename
                          file->lines
                          (map line->card)
                          (map assoc-num-matches)
                          (reduce (fn [m {:keys [id num-matches]}]
                                    (assoc m id num-matches)) {}))
        ids (->> scores-by-id count range (map inc))
        starting-state (into {} (map vector ids (repeat 1)))]
    (->> (reduce (fn [acc id]
                   (let [id-total (get acc id)
                         followers (->> (get scores-by-id id) range (map #(+ % id 1)))]
                     (reduce (fn [acc2 follower]
                               (update acc2 follower + id-total))
                             acc
                             followers)))
                 starting-state
                 ids)
         vals
         (reduce +))))

;;;

(advent/defrunner 1 file->score-sum "Score sum")
(advent/defrunner 2 file->card-total "Total cards")
