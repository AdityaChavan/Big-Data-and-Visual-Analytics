import csv
import json
import time
import tweepy


# You must use Python 2.7.x

# 1 point
def loadKeys(key_file):
    # TODO: put your keys and tokens in the keys.json file,
    #       then implement this method for loading access keys and token from keys.json
    # rtype: str <api_key>, str <api_secret>, str <token>, str <token_secret>

    # Load keys here and replace the empty strings in the return statement with those keys
    key = json.load(open(key_file))
    api_secret=key["api_secret"]
    api_key=key["api_key"]
    token=key["token"]
    token_secret=key["token_secret"]
    return api_key,api_secret,token,token_secret

        
# 4 points
def getPrimaryFriends(api, root_user, no_of_friends):
    # TODO: implement the method for fetching 'no_of_friends' primary friends of 'root_user'
    # rtype: list containing entries in the form of a tuple (root_user, friend)
    primary_friends = []
    # Add code here to populate primary_friends
    user=""
    while not user:
            try:
                print('trying for '+root_user)
                user=api.get_user(root_user)
                for friends in user.friends(count=no_of_friends):
                    primary_friends.append(tuple([user.screen_name]+[friends.screen_name]))
                print('primary_friends done')    
            except tweepy.RateLimitError:
                print('Rate Limit Error')
                time.sleep(1 * 30)
                user=""
        
    return primary_friends

# 4 points
def getNextLevelFriends(api, users_list, no_of_friends):
    print(users_list)
    # TODO: implement the method for fetching 'no_of_friends' friends for each user in users_list
    # rtype: list containing entries in the form of a tuple (user, friend)
    next_level_friends = []
    for users in users_list:
        print('trying for '+users[1])    
        user=""
        while not user:  
            try:
                user=api.get_user(users[1])
                for friends in user.friends(count=no_of_friends):
                    next_level_friends.append(tuple([user.screen_name]+[friends.screen_name]))
                print(users[1]+" done")    
            except tweepy.RateLimitError:
                print('Rate Limit Error')
                time.sleep(1 * 60)
                user=""
            
    # Add code here to populate next_level_friends
    return next_level_friends

# 4 points
def getNextLevelFollowers(api, users_list, no_of_followers):
    # TODO: implement the method for fetching 'no_of_followers' followers for each user in users_list
    # rtype: list containing entries in the form of a tuple (follower, user)    
    next_level_followers = []
    # Add code here to populate next_level_followers
    for users in users_list:
        print('trying for '+users[1])    
        user=""
        while not user:  
            try:
                user=api.get_user(users[1])
                for followers in user.followers(count=no_of_followers):
                    next_level_followers.append(tuple([followers.screen_name]+[user.screen_name]))
                print(users[1]+" done")    
            except tweepy.RateLimitError:
                print('Rate Limit Error')
                time.sleep(1 * 60)
                user=""
            
    
    return next_level_followers

# 3 points
def GatherAllEdges(api, root_user, no_of_neighbours):
    # TODO:  implement this method for calling the methods getPrimaryFriends, getNextLevelFriends
    #        and getNextLevelFollowers. Use no_of_neighbours to specify the no_of_friends/no_of_followers parameter.
    #        NOT using the no_of_neighbours parameter may cause the autograder to FAIL.
    #        Accumulate the return values from all these methods.
    # rtype: list containing entries in the form of a tuple (Source, Target). Refer to the "Note(s)" in the 
    #        Question doc to know what Source node and Target node of an edge is in the case of Followers and Friends. 
    all_edges = [] 
    #Add code here to populate all_edges
    users_list=getPrimaryFriends(api, root_user, no_of_neighbours)
    list_2=getNextLevelFriends(api, users_list, no_of_neighbours)
    list_3=getNextLevelFollowers(api, users_list, no_of_neighbours)
    all_edges.append(users_list)
    all_edges.append(list_2)
    all_edges.append(list_3)
    print(len(users_list))
    print(len(list_2))
    print(len(list_3))
    
    return all_edges



# 2 points
def writeToFile(data, output_file):
    # write data to output_file
    # rtype: None
    with open(output_file,'w') as myFile:
            writer=csv.writer(myFile)
            for element in data:
                    writer.writerows(element)
    myFile.close()                
    pass




"""
NOTE ON GRADING:

We will import the above functions
and use testSubmission() as below
to automatically grade your code.

You may modify testSubmission()
for your testing purposes
but it will not be graded.

It is highly recommended that
you DO NOT put any code outside testSubmission()
as it will break the auto-grader.

Note that your code should work as expected
for any value of ROOT_USER.
"""

def testSubmission():
    KEY_FILE = 'keys.json'
    OUTPUT_FILE_GRAPH = 'graph.csv'
    NO_OF_NEIGHBOURS = 20
    ROOT_USER = 'PoloChau'

    api_key, api_secret, token, token_secret = loadKeys(KEY_FILE)

    auth = tweepy.OAuthHandler(api_key, api_secret)
    auth.set_access_token(token, token_secret)
    api = tweepy.API(auth)

    edges = GatherAllEdges(api, ROOT_USER, NO_OF_NEIGHBOURS)

    writeToFile(edges, OUTPUT_FILE_GRAPH)
    
    print('Done')
if __name__ == '__main__':
    testSubmission()

        
