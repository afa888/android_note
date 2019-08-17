##static navaigationOptions中调用this或者方法，报错undefined



```
static navigationOptions = ({navigation}) => {
    const {params} = navigation.state;
    return {
        headerRight: (
            <TouchableOpacity onPress={() => {
                if (navigation.state.params !== undefined) {
                    navigation.state.params.choseType();
                }
            }}>
                <Image
                    source={require('../../static/img/more.png')}
                    style={{
                        resizeMode: 'contain',
                        width: 20,
                        height: 20,
                        marginRight: 12
                    }}/>
            </TouchableOpacity>
        ),
    };
};

 componentWillMount() {
   	  this.props.navigation.setParams({choseType:this.choseType})
 }
        
        
 choseType = () => {
 		cosole.log("被调用了")
 }
```

第一次传过去的params是undefined，这里有个坑就是一定要判断navigation.state.params !== undefined。