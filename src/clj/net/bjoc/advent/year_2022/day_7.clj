(ns net.bjoc.advent.year-2022.day-7
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]
            [net.bjoc.advent.util.misc :refer [file->lines keys-in]]))

(defn partition-by-command [lines]
  (let [same-command? (complement #(re-find #"^\$ " %))
        remaining (drop-while same-command? (rest lines))
        curr {:input (first lines)
              :output (take-while same-command? (rest lines))}]
    (lazy-seq (cons curr (when-not (empty? remaining)
                           (partition-by-command remaining))))))

(defn command-type [input]
  (cond
    (re-find #"^\$ ls" input) :ls
    (re-find #"^\$ cd" input) :cd
    :else (throw (ex-info "Unrecognized command" {:input input}))))

(defn- evolve-path [path arg]
  (cond
    (= arg "/") []
    (= arg "..") (pop path)
    :else (conj path arg)))

(def parse-args #(second (re-find #"^\$ \w+\s?(.*)" %)))

(defn evolve
  [{:keys [path] :as state} {:keys [input output]}]
  (case (command-type input)
    :cd (update state :path evolve-path (parse-args input))
    :ls (reduce (fn [state line]
                  (let [[raw-val name] (str/split line #"\s+")
                        v (if (= raw-val "dir") {} (Integer/parseInt raw-val))]
                    (assoc-in state (concat [:fs] path [name]) v)))
                state output)))

(defn file->fs [filename]
  (->> filename
       file->lines
       partition-by-command
       (reduce evolve {:path [] :fs {}})
       :fs))

(defn size [fs path]
  (let [v (get-in fs path)]
    (cond
      (map? v) (let [paths (map (partial conj path) (keys v))]
                 (apply + (map (partial size fs) paths)))
      (int? v) v)))

(defn file->selective-dir-sum [filename]
  (let [fs (file->fs filename)
        dir? #(map? (get-in fs %))]
    (->> fs
         keys-in
         (filter dir?)
         (map (partial size fs))
         (remove #(> % 100000))
         (reduce +))))

;;;

(defn file->best-dir-size
  [filename
   & {:keys [disk-size size-needed]
      :or {disk-size 70000000
           size-needed 30000000}
      :as options}]
  (let [fs (file->fs filename)
        dir? #(map? (get-in fs %))
        free-space (- disk-size (size fs []))
        target (- size-needed free-space)]
    (->> fs
         keys-in
         (filter dir?)
         (map (partial size fs))
         (filter #(>= % target))
         sort
         first)))

;;;

(advent/defrunner 1 file->selective-dir-sum "Sum of dirs with size <100K")
(advent/defrunner 2 file->best-dir-size "Size of the best directory to delete")
