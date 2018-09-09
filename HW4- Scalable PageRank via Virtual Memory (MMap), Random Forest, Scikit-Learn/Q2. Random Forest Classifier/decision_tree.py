from util import entropy, information_gain, partition_classes
import numpy as np 
import ast

class DecisionTree(object):
    def __init__(self):
        # Initializing the tree as an empty dictionary or list, as preferred
        #self.tree = []
        self.tree = {}
        #pass

    def learn(self, X, y):
        self.tree = self.create_new_branch(X,y);


    def create_new_branch(self,X,y):
        #print("enter %d %d"  %(len(X),len(y)))
        if len(set(y))==1:#all elements same
            #self.tree["label"] = "same";
            print("same")
            return y[0]
        if((len(X[0])) < 1):
           return 1 if(y.count(1)>y.count(0)) else 0
            
        

        #finding best node
        (val,counts)=np.unique(y,return_counts=True);
        index = np.argmax(counts);
        X_right=[];
        X_left=[];
        y_right=[];
        y_left=[];
        if(isinstance(X[0][index],int)): #is it an int
           mean=X[0][index]/2;
        else:
            mean=X[0][index];
           
        (X_left,X_right,y_left,y_right)=partition_classes(X,y,index,mean);
        #print("Split %d %d; X- %d %d;y- %d %d;" %(len(X),len(y),len(X_left),len(X_right),len(y_left),len(y_right)));
        if len(X_left)==0 or len(X_right)==0 :
            #max(y)
            a=1 if(y.count(1)>y.count(0)) else 0
            #print("Exited %d %d" %(len(X_left),len(X_right)))
            return a
        else:
            tree={}
            tree[index]=[mean,self.create_new_branch(X_right , y_right),
            self.create_new_branch(X_left , y_left )]
            return tree
                         
        pass


    def classify(self, record):
        while isinstance(self.tree, dict):#iterate thru list
            feature = list(self.tree.keys())[0]
            if record[feature] >= self.tree[feature][0]:
                self.tree = self.tree[feature][2]
            else:
                self.tree = self.tree[feature][1]
        return self.tree
        pass
