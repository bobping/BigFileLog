for i in ./lib/*.jar ; do
  export CLASSPATH=$i:$CLASSPATH
done
for i in ./lib/curator/*.jar ; do
  export CLASSPATH=$i:$CLASSPATH
done
for i in ./lib/spring/*.jar ; do
  export CLASSPATH=$i:$CLASSPATH
done
for i in ./lib/fqueue/*.jar ; do
  export CLASSPATH=$i:$CLASSPATH
done
export dependClasses=./bin:$dependClasses
nohup java -DPGM_ID=LogFileServer -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -cp :$dependClasses:$CLASSPATH: com.ztesoft.server.ServerStart >nohup.out &

sleep 2

tail -f nohup.out