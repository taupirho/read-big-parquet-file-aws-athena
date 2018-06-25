# read-big-parquet-file-aws-athena
A comparison between reading a big text data file and its parquet equivalent using Athena

So, I wrote a previous repo - check it out [here](https://github.com/taupirho/read-big-file-aws-athena-glue) - where I detailed 
some timings on reading a big pipe separated text file using AWS Athena and Glue. I thought it would be interesting to repeat the 
same SQL's against the same file, but this time in parquet format. As a recap the original file was a pipe sparated text file, 
21Gbytes uncompressed and contained approx 366 million records. I ran the following SQL statements against and got the timings as shown

# Timings for reading a text file
select count(*), periodid from holdings group by periodid    
15.83 seconds

select count(*) from holdings where periodid = 56      
14.37 seconds to return a value of 7,841,105 records

/* return the SUM of the numeric sixth field (sharesheld) in my file */                                                        
select sum(sharesheld) from holdings     
19.68 seconds to return the value of 170,237,428,853,225,337 

select * from holdings where periodid = 56        
42 seconds

How much difference would having the same file in parquet format make. Well, quite a lot as you'll see in a moment. First of all 
though I had to convert my original text file to parquet format. For those of you don't know parquet is a popular compressed 
columnar data storage format. To convert my file I wrote a spark/scala program, an ran it on an eclipse IDE on my local PC 
against a local copy of my original text file. The job took about 35 minutes to run and prodcuced 161 separate parquet files totaling
about 4 Gbytes on my PC which I uploaded to an S3 bucket. The upload to S3 took about 2 hours to run. The progarm to convert to parquet 
is included with this repo. After the upload had finished the next step was to run a AWS Glue crawler against the bucket containing my 
parquet files, and create a database "table" containg metatdata about the files so that Athena would know what they were all about. After 
that it was simply a case of re-running the above queries. The timings were very suprising - in a good way.

# Timings for reading the equivalent parquet file
select count(*), periodid from holdings group by periodid    
1.92 seconds

select count(*) from holdings where periodid = 56      
0.86 seconds to return a value of 7,841,105 records

/* return the SUM of the numeric sixth field (sharesheld) in my file */                                                        
select sum(sharesheld) from holdings     
1 second to return the value of 170,237,428,853,225,337 

select * from holdings where periodid = 56        
51 seconds
