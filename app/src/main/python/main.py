import requests

def get_posts(count, url):
    token = 'c8a9a3dbc8a9a3dbc8a9a3db82c8d1ac77cc8a9c8a9a3dba81d4f21975d685893981a54'
    version = 5.131
    posts = []



    response = requests.get('https://api.vk.com/method/wall.get',
                        params = {
                            'access_token': token,
                            'v': version,
                            'domain': url,
                            'count': count,
                        }
                        )
    data = response.json()['response']['items']
    posts.extend(data)

    return parse_to_string(posts)

def parse_to_string(posts):
    line = ''
    for i in range(len(posts)):
        try:
            line += posts[i]['text']
        except:
            pass

    return line

# posts = get_posts(100, 'bugurt_thread')
# s = parse_to_string(posts)
# print(s)