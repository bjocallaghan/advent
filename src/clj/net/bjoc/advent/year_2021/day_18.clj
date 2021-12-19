(ns net.bjoc.advent.year-2021.day-18
  (:use [net.bjoc.advent.util.misc :only [zip find-first]])
  (:require [clojure.string :as str]
            [net.bjoc.advent.core :as advent]))

(defn update-in*
  "'Updates' a value in a nested associative structure, where ks is a sequence of
  keys and f is a function that will take the old value and any supplied args
  and return the new value, and returns a new nested structure. If any levels do
  not exist, no operation will take place, and the original associative
  structure will be returned.

  This is essentially a 'safe' variation of update-in."
  [m ks f & args]
  (if (and (get-in m ks) (not (empty? ks)))
    (apply (partial update-in m ks f) args)
    m))

;;;

(defn- inc-last
  "Increment the last value in a vector."
  [v]
  (-> v pop (conj (-> v last inc))))

(defn- child [address] (conj address 0))
(defn- sib [address] (when (not-empty address) (inc-last address)))
(defn- cousin [m address]
  (when (and (not-empty address) (not-empty (pop address)))
    (let [addr (-> address pop inc-last)]
      (if (get-in m addr)
        addr
        (recur m addr)))))

(defn- next-addr [m address]
  (let [next-addr (find-first #(get-in m %) [(child address)
                                             (sib address)
                                             (cousin m address)])]
    (when-not (nil? next-addr)
      (if (number? (get-in m next-addr))
        next-addr
        (recur m next-addr)))))

(defn dfs-leaf-addresses
  ([m] (dfs-leaf-addresses m (next-addr m [])))
  ([m address]
   (let [next (next-addr m address)]
     (lazy-seq (cons address (when next (dfs-leaf-addresses m next)))))))

(defn sf-can-explode? [m addr]
  (and (> (count addr) 4)
       (number? (get-in m (inc-last addr)))))

(defn sf-can-split? [m addr]
  (>= (get-in m addr) 10))

(defn surrounding-addresses [m address]
  (let [addrs (dfs-leaf-addresses m)]
    (->> (zip addrs (concat [nil] addrs))
         (drop-while #(not= (first %) address))
         (take 4)
         (map second))))

(defn surrounding-addresses [m address]
  (let [addrs (dfs-leaf-addresses m)]
    (->> (zip (concat addrs [nil]) (concat [nil] addrs))
         (drop-while #(not= (first %) address))
         (take 4)
         (map second))))

(defn sf-explode-at [m addr]
  (let [[a-addr b-addr c-addr d-addr] (surrounding-addresses m addr)
        [b c] (map #(get-in m %) [b-addr c-addr])
        res (-> (assoc-in m (pop b-addr) 0)
                (update-in* a-addr + b)
                (update-in* d-addr + c))]
    {:m res
     :restart-addr (or a-addr (next-addr res []))}))

(defn sf-split-at [m addr]
  (let [x (get-in m addr)
        half (quot x 2)]
    {:m (assoc-in m addr [half (if (even? x) half (inc half))])
     :restart-addr (conj addr 0)}))

;; (defn sf-reduce
;;   ([sf-number] (sf-reduce sf-number (-> sf-number dfs-leaf-addresses first)))
;;   ([sf-number starting-addr]
;;    (let [{:keys [m restart-addr] :as sf-num}
;;          (reduce (fn [m addr]
;;                    (cond
;;                      (sf-can-explode? m addr) (reduced (sf-explode-at m addr))
;;                      (sf-can-split? m addr) (reduced (sf-split-at m addr))
;;                      :else m))
;;                  sf-number (dfs-leaf-addresses sf-number starting-addr))]
;;      (if restart-addr
;;        (recur m restart-addr)
;;        sf-num))))

;; Things got messy here. I realized that my approach (above) was producing
;; incorrect answers because it was finding splits to do before doing ALL of the
;; possible explosions. The rules state that all explosions occur, then splits,
;; then re-evaluate. Instead of truly rethinking / refactoring, I copy/pasted
;; and shoved some code around.

(defn sf-reduce
  ([sf-number] (sf-reduce sf-number (-> sf-number dfs-leaf-addresses first)))
  ([sf-number starting-addr]
   (let [{:keys [m restart-addr] :as sf-num}
         (reduce (fn [m addr]
                   (cond
                     (sf-can-explode? m addr) (reduced (sf-explode-at m addr))
                     :else m))
                 sf-number (dfs-leaf-addresses sf-number starting-addr))]
     (if restart-addr
       (recur m restart-addr)
       (let [starting-addr (-> sf-number dfs-leaf-addresses first)
             {:keys [m restart-addr] :as sf-num}
             (reduce (fn [m addr]
                       (cond
                         (sf-can-split? m addr) (reduced (sf-split-at m addr))
                         :else m))
                     sf-number (dfs-leaf-addresses sf-number starting-addr))]
         (if restart-addr
           (recur m restart-addr)
           sf-num))))))

(defn sf-add [& args]
  (sf-reduce (apply vector args)))

(defn sf-add [& args]
  (reduce #(sf-reduce (vector %1 %2)) (first args) (rest args)))

(defn mag [[a b]]
  (+ (* 3 (if (number? a) a (mag a)))
     (* 2 (if (number? b) b (mag b)))))

(defn file->sum-mag [filename]
  (let [lines (-> filename
                  slurp
                  (str/split #"\n"))]
    (->> lines
         (map read-string)
         (apply sf-add)
         mag)))
  
;;;

(defn file->best-sum-mag [filename]
  (let [lines (-> filename
                  slurp
                  (str/split #"\n"))
        sf-nums (->> lines (map read-string))]
    (apply max (for [x sf-nums
                       y sf-nums
                       :when (not= x y)]
                   (mag (sf-add x y))))))

;;;

(advent/defrunner 1 file->sum-mag "Magnitude of the final sum")
(advent/defrunner 2 file->best-sum-mag "Magnitude of the best combination")
