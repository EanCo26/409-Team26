public class example {

    int test = 0;
    int test2 = 0;
    public example(){

    }

    public void pair1(){
        test=0;
        test2 = 0;
    }

    public void pair2(){
        test=0;
        test2 = 0;
    }

    public void pair3(){
        test=0;
        test2 = 0;
    }

    public void CheckIfs(int a, int b) {
        int result = a + b;
        if(result>0 || result == 0) {
            String colour = "red";
        }
        else if(result<0){
            String colour = "blue";
        }

        if(i<4){

        }
        else{
            return;
        }
    }

    public void CheckBody();

    public int CheckSwitch(int x, int y){
        int r = x;
        switch (r) {
            case x:
                r -= x;
                return r;
            case y:
                r += y;
                return r;
            default:
                break;
        }
        return r;
    }

    public int CheckLoops(int x, int y){
        while(x == 0 && y >50){
            x ++;
            while(y >50) {
                y--;
            }
        }
        for(int i = 0; i++; i<5){

        }
        for (int j: list) {

        }
        do{
            x = 7;
        }while (x<5 || x>10);
        return x;
    }
}