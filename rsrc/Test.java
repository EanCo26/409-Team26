public class example {
    public void Sum(int a, int b) {
        int result = a + b;
        if(result>0) {
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

    public int Check(int x, int y){
        while(x!=y){
            if(x>y){
                x=x-y;
            }
            else{
                y=y-x;
            }
        }
        return x;
    }

    public int CheckAnother(int x, int y){
        int r = x;
        if(x > 5){
            r = 5;
            return r;
        }
        if(y<5){
            r = y;
            return r;
        }
        return r;
    }

    public void Last(){

    }
}