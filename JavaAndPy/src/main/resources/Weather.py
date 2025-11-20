import requests #发送请求
from lxml import etree #数据预处理
import csv #写入csv文件

def getWeather(url):
    weather_info = []
    headers = {
        'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/136.0.0.0 Safari/537.36 Edg/136.0.0.0'
    }
    #请求
    resp = requests.get(url,headers=headers)
    #数据预处理
    resp_html = etree.HTML(resp.text)
    #xpath提取所有数据
    resp_list = resp_html.xpath("//ul[@class='thrui']/li")
    #对于每一天数据进行遍历
    for li in resp_list:
        #每天天气数据封装
        day_weather_info = {}
        day_weather_info['date_time'] = li.xpath("./div[1]/text()")[0].split(' ')[0]
        high = li.xpath("./div[2]/text()")[0]
        day_weather_info['high'] = high[:high.find('℃')]
        low = li.xpath("./div[3]/text()")[0]
        day_weather_info['low'] = low[:low.find('℃')]
        day_weather_info['weather'] = li.xpath("./div[4]/text()")[0]
        #放入每月天气数据
        weather_info.append(day_weather_info)
    return weather_info

weathers = []
for month in range(1,13):
    city_name = "nanchang"
    weather_time = '2023' + ('0' + str(month) if month < 10 else str(month))
    url = f'https://lishi.tianqi.com/{city_name}/{weather_time}.html'
    weather = getWeather(url)
    weathers.append(weather)
print(weathers)

with open("weather.csv","w", newline='') as csvfile:
    writer = csv.writer(csvfile)

    writer.writerow(["日期","最高日期","最低气温",'天气'])
    writer.writerows([list(day_weather_dict.values()) for month_weather in weathers for day_weather_dict in month_weather])