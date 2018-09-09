## Data and Visual Analytics - Homework 4
## Georgia Institute of Technology
## Applying ML algorithms to recognize seizure from EEG brain wave signals

import numpy as np
import pandas as pd
import time 

from sklearn.model_selection import cross_val_score, GridSearchCV, cross_validate, train_test_split
from sklearn.metrics import accuracy_score, classification_report
from sklearn.svm import SVC
from sklearn import linear_model
from sklearn.neural_network import MLPClassifier
from sklearn.ensemble import RandomForestClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.preprocessing import normalize
import warnings
warnings.filterwarnings("ignore")#for supressing warnings
######################################### Reading and Splitting the Data ###############################################

# Read in all the data.
data = pd.read_csv('seizure_dataset.csv')

# Separate out the x_data and y_data.
x_data = data.loc[:, data.columns != "y"]
y_data = data.loc[:, "y"]

# The random state to use while splitting the data. DO NOT CHANGE.
random_state = 100 # DO NOT CHANGE

# XXX
# TODO: Split each of the features and labels arrays into 70% training set and
#       30% testing set (create 4 new arrays). Call them x_train, x_test, y_train and y_test.
#       Use the train_test_split method in sklearn with the paramater 'shuffle' set to true 
#       and the 'random_state' set to 100.
# XXX
x_train, x_test, y_train, y_test = train_test_split(x_data,y_data,test_size=0.7,shuffle=True);


# ############################################### Linear Regression ###################################################
# XXX
# TODO: Create a LinearRegression classifier and train it.
# XXX


# Create linear regression object
regr = linear_model.LinearRegression()

# Train the model using the training sets
regr.fit(x_train, y_train)

# Make predictions using the testing set
y_predict = regr.predict(x_test)

# XXX
# TODO: Test its accuracy (on the testing set) using the accuracy_score method.
# Note: Use y_predict.round() to get 1 or 0 as the output.
# XXX
a1=accuracy_score(y_test, y_predict.round());
print("Linear Regression: %f" %a1)



#a= 0.810359964881

# ############################################### Multi Layer Perceptron #################################################
# XXX
# TODO: Create an MLPClassifier and train it.
# XXX

mlp = MLPClassifier()
mlp.fit(x_train, y_train)
y_predict = mlp.predict(x_test)
a2=accuracy_score(y_test, y_predict.round());
print("\nMLP: %f" %a2);




# ############################################### Random Forest Classifier ##############################################
# XXX
# TODO: Create a RandomForestClassifier and train it.
# XXX

rfc = RandomForestClassifier();
rfc.fit(x_train, y_train)
y_predict = rfc.predict(x_test)
a3=accuracy_score(y_test, y_predict.round());
print("\nRandom Forest: %f" %a3);



# XXX
# TODO: Tune the hyper-parameters 'n_estimators' and 'max_depth' 
#       and select the combination that gives the best testing accuracy.
#       After fitting, print the best params, using .best_params_, and print the best score, using .best_score_.
# XXX

params={"n_estimators": [10,20,30],"max_depth":[100,150,200]}
rfc2 = RandomForestClassifier()
#rfc2.fit(x_train, y_train)
grid = GridSearchCV(rfc2, params,scoring='accuracy',cv=10)
grid.fit(x_train,y_train)
print("\nRandom Forest Hypertuning Best Parameters: ");
print(grid.best_params_);
a4=grid.best_score_;
print("Random Forest Hypertuning Accuracy: %f " %a4);

# ############################################ Support Vector Machine ###################################################
# XXX
# TODO: Create a SVC classifier and train it.
# XXX

# XXX
# TODO: Test its accuracy on the test set using the accuracy_score method.
# XXX

svc = SVC()
svc.fit(x_train, y_train)
y_predict = svc.predict(x_test)
a5=accuracy_score(y_test, y_predict.round());
print("\nSVM: %f" %a5 );



# XXX
# TODO: Tune the hyper-parameters 'C' and 'kernel' (use rbf and linear) 
#       and select the set of parameters that gives the best testing accuracy.
#       After fitting, print the best params, using .best_params_, and print the best score, using .best_score_.
# XXX
x_norm=normalize(x_train)



svc = SVC()
svc.fit(x_norm, y_train)

params={"C": [0.001,0.1,10], "kernel":['linear','rbf']}
grid = GridSearchCV(svc, params,scoring='accuracy',cv=10)
grid.fit(x_norm,y_train)
print("\nSVM Best Parameters: ");
print(grid.best_params_);
a6=grid.best_score_;
print("SVM Hypertuning Accuracy: %f " %a6);
#print("cv_results : " )
#print(grid.cv_results_.get('mean_train_score'))
#print(grid.cv_results_.get('mean_test_score'))
#print(grid.cv_results_.get('mean_fit_time'))


# XXX 
# ########## PART C ######### 
# TODO: Print your CV's best mean testing accuracy and its corresponding mean training accuracy and mean fit time.
# 		State them in report.txt
# XXX
#params={"C": [0.001], "kernel":['linear']}
svc = SVC(C=0.001,kernel="linear")
svc.fit(x_train, y_train)

scaler = StandardScaler()
scaler.fit(x_data)
x_standard= scaler.transform(x_test)
x_standard2= scaler.transform(x_train)


y_predict = svc.predict(x_standard)
a5=accuracy_score(y_test, y_predict.round());
print("\nSVM with standardized data: %f" %a5 );



