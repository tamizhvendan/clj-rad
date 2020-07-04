(ns component
  (:require [hiccup.core :as hiccup]
            [clojure.walk :as walk]))

(defn child []
  [:p "child"])

(defn parent []
  [:div
   [:p "parent"]
   [child]])

(defrecord ChildComponent [component])

(defn walk-and-transform [tree]
  (walk/postwalk
   (fn [x]
     (cond
       (fn? x) (->ChildComponent (x))
       (and (vector? x) (instance? ChildComponent (first x))) (:component (first x))
       :else x))
   tree))

(defn html [component]
  (hiccup/html (walk-and-transform (component))))

(walk-and-transform (parent))
(html parent)