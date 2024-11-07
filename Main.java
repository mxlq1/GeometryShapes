import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args){
        System.out.print("");
    }

    record Point(double x, double y) {

        public Point add(Point B){
            double x_1 = this.x + B.x;
            double y_1 = this.y + B.y;

            return new Point(x_1, y_1);
        }

        public Point subtract(Point B){
            double x_1 = this.x - B.x;
            double y_1 = this.y - B.y;

            return new Point(x_1, y_1);
        }


        public Point multiply(double k){
            double x_1 = this.x * k;
            double y_1 = this.y * k;

            return new Point(x_1, y_1);
        }

        public Point divide(double k){
            double x_1 = this.x / k;
            double y_1 = this.y / k;

            return new Point(x_1, y_1);
        }

        public Point rotate(Point O, double angle){
            Point OA = this.subtract(O);

            double x_1 = OA.x * Math.cos(angle) - OA.y * Math.sin(angle);
            double y_1 = OA.x * Math.sin(angle) + OA.y * Math.cos(angle);

            return new Point(x_1, y_1).add(O);
        }

        public Point scale(Point O, double coefficient){ // A - given Point (this)
            Point OA = this.subtract(O);
            OA = OA.multiply(coefficient);

            return OA.add(O);
        }

        public double distance(Point B){
            return Math.sqrt(Math.pow(this.x - B.x, 2) + Math.pow(this.y - B.y, 2));
        }
    }
    abstract class Shape {
        abstract Point center();
        abstract double perimeter();
        abstract double area();

        abstract void translate(final Point newCenter);
        abstract void rotate(final double angle);
        abstract void scale(final double coefficient);
    }
    class Ellipse extends Shape{

        private Point F1;
        private Point F2;
        private double prefocus;

        public Ellipse(Point F1, Point F2, double prefocus){
            this.F1 = F1;
            this.F2 = F2;
            this.prefocus = prefocus;
        }

        public List<Point> focuses(){
            List<Point> focus_list = new ArrayList<>();
            focus_list.add(this.F1);
            focus_list.add(this.F2);

            return focus_list;
        }

        public double focalDistance(){
            return this.F1.distance(this.F2) / 2;
        }

        public double majorSemiAxis (){
            return (this.F1.distance(this.F2) + 2 * this.prefocus) / 2;
        }

        public double minorSemiAxis(){
            return this.majorSemiAxis() * Math.sqrt(1 - Math.pow(this.eccentricity(), 2));
        }

        public double eccentricity(){
            return this.focalDistance() / this.majorSemiAxis();
        }

        @Override
        public Point center() {
            return new Point((F1.x() + F2.x()) / 2, (F1.y() + F2.y()) / 2);
        }

        @Override
        public double perimeter() {
            double a = this.majorSemiAxis();
            double b = this.minorSemiAxis();

            return 4 * ((Math.PI * a * b + Math.pow(a - b, 2)) / (a + b));
        }

        @Override
        public double area() {
            return Math.PI * this.majorSemiAxis() * this.minorSemiAxis();
        }

        @Override
        public void translate(Point newCenter) {
            double dx = this.center().x() - newCenter.x();
            double dy = this.center().y() - newCenter.y();

            this.F1 = new Point(this.F1.x() - dx, this.F1.y() - dy);
            this.F2 = new Point(this.F2.x() - dx, this.F2.y() - dy);
        }

        @Override
        public void rotate(double angle) {

            /*Point OF1 = this.F1.subtract(O);
            Point OF2 = this.F2.subtract(O);

            OF1 = OF1.rotate(angle);
            OF2 = OF2.rotate(angle);

            this.F1 = OF1.add(O);
            this.F2 = OF2.add(O);*/

            Point O = this.center();

            this.F1 = F1.rotate(O, angle);
            this.F2 = F2.rotate(O, angle);

        }

        @Override
        public void scale(double coefficient) {
            Point O = this.center();
            this.F1 = this.F1.scale(O, coefficient);
            this.F2 = this.F2.scale(O, coefficient);
            this.prefocus *= Math.abs(coefficient);
        }
    }
    class Circle extends Ellipse{

        public Circle(Point O, double R) {
            super(O, O, R);
        }

        public double radius(){
            return this.majorSemiAxis();
        }
    }
    class Rectangle extends Shape{

        private Point A;
        private Point B;
        private double width;

        public Rectangle(Point A, Point B, double width){
            this.A = A;
            this.B = B;
            this.width = width;
        }

        public List<Point> vertices(){
            List<Point> vertices_list = new ArrayList<>();

            if (Math.abs(A.x() - B.x()) < Math.pow(10, -4)){
                if (A.y() - B.y() > Math.pow(10, -4)){ // B .. A
                    vertices_list.add(new Point(A.x() - (this.width / 2), A.y()));//A
                    vertices_list.add(new Point(B.x() - (this.width / 2), B.y()));//B
                    vertices_list.add(new Point(B.x() + (this.width / 2), B.y()));//C
                    vertices_list.add(new Point(A.x() + (this.width / 2), A.y()));//D
                }
                else{ // A .. B
                    vertices_list.add(new Point(A.x() + (this.width / 2), A.y()));//D
                    vertices_list.add(new Point(B.x() + (this.width / 2), B.y()));//C
                    vertices_list.add(new Point(B.x() - (this.width / 2), B.y()));//B
                    vertices_list.add(new Point(A.x() - (this.width / 2), A.y()));//A
                }
            }
            else if (Math.abs(A.y() - B.y()) < Math.pow(10, -4)){
                if (A.x() - B.x() > Math.pow(10, -4)){
                    vertices_list.add(new Point(A.x(), A.y() + (this.width / 2)));//D
                    vertices_list.add(new Point(B.x(), B.y() + (this.width / 2)));//C
                    vertices_list.add(new Point(B.x(), B.y() - (this.width / 2)));//B
                    vertices_list.add(new Point(A.x(), A.y() - (this.width / 2)));//A
                }
                else{
                    vertices_list.add(new Point(A.x(), A.y() - (this.width / 2)));//A
                    vertices_list.add(new Point(B.x(), B.y() - (this.width / 2)));//B
                    vertices_list.add(new Point(B.x(), B.y() + (this.width / 2)));//C
                    vertices_list.add(new Point(A.x(), A.y() + (this.width / 2)));//D
                }
            }
            else{
                double m = (A.x() - B.x()) / (B.y() - A.y());
                double dx = (this.width / (Math.sqrt(1 + (m*m)))) * 0.5;
                double dy = m * dx;
                double k = (B.y() - A.y()) / (B.x() - A.x());

                if ((A.x() - B.x() > Math.pow(10, -4) && k > Math.pow(10, -4)) || (A.x() - B.x() < (-1) * Math.pow(10, -4) && k < (-1) * Math.pow(10, -4))){
                    vertices_list.add(new Point(A.x() - dx, A.y() - dy));//A
                    vertices_list.add(new Point(B.x() - dx, B.y() - dy));//B
                    vertices_list.add(new Point(B.x() + dx, B.y() + dy));//C
                    vertices_list.add(new Point(A.x() + dx, A.y() + dy));//D
                }
                else{
                    vertices_list.add(new Point(A.x() + dx, A.y() + dy));//D
                    vertices_list.add(new Point(B.x() + dx, B.y() + dy));//C
                    vertices_list.add(new Point(B.x() - dx, B.y() - dy));//B
                    vertices_list.add(new Point(A.x() - dx, A.y() - dy));//A
                }
            }
            return vertices_list;
        }

        public double firstSide(){
            return this.A.distance(this.B);
        }

        public double secondSide(){
            return this.width;
        }

        public double diagonal(){
            Point C = this.vertices().get(0);
            Point E = this.vertices().get(2);

            return C.distance(E);
        }


        @Override
        public Point center() {
            return this.A.add(this.B).divide(2);
        }

        @Override
        public double perimeter() {
            return 2 * this.firstSide() + 2 * this.secondSide();
        }

        @Override
        public double area() {
            return this.firstSide() * this.secondSide();
        }

        @Override
        public void translate(Point newCenter) {
            double dx = this.center().x() - newCenter.x();
            double dy = this.center().y() - newCenter.y();

            this.A = new Point(this.A.x() - dx, this.A.y() - dy);
            this.B = new Point(this.B.x() - dx, this.B.y() - dy);
        }

        @Override
        public void rotate(double angle) {
            Point O = this.center();

            this.A = this.A.rotate(O, angle);
            this.B = this.B.rotate(O, angle);
        }

        @Override
        public void scale(double coefficient) {
            Point O = this.center();
            this.A = this.A.scale(O, coefficient);
            this.B = this.B.scale(O, coefficient);
            this.width *= Math.abs(coefficient);
        }
    }
    class Square extends Rectangle{

        public Square(Point A, Point B){
            super(A, B, A.distance(B));
        }

        public double side(){
            return this.secondSide();
        }

        public Circle circumscribedCircle(){
            return new Circle(this.center(), this.diagonal() / 2);
        }

        public Circle inscribedCircle(){
            return new Circle(this.center(), this.secondSide() / 2);
        }
    }
    class Triangle extends Shape{

        private Point A;
        private Point B;
        private Point C;
        private double AB;
        private double BC;
        private double AC;

        public Triangle(Point A, Point B, Point C){
            this.A = A;
            this.B = B;
            this.C = C;
            this.AB = this.A.distance(this.B);
            this.BC = this.B.distance(this.C);
            this.AC = this.A.distance(this.C);
        }



        public List<Point> vertices(){
            List<Point> vertices_list = new ArrayList<>();
            vertices_list.add(this.A);
            vertices_list.add(this.B);
            vertices_list.add(this.C);

            return vertices_list;
        }

        public Circle circumscribedCircle(){
        /*
        (x1-a)^2 + (y1-b)^2 = r^2
        (x2-a)^2 + (y2-b)^2 = r^2
        (x3-a)^2 + (y3-b)^2 = r^2
         */

            double x1 = this.A.x();
            double y1 = this.A.y();
            double x2 = this.B.x();
            double y2 = this.B.y();
            double x3 = this.C.x();
            double y3 = this.C.y();

            double n = Math.pow(x2, 2) - Math.pow(x3, 2) + Math.pow(y2, 2) - Math.pow(y3, 2); // для сокращения
            double m = Math.pow(x1, 2) - Math.pow(x2, 2) + Math.pow(y1, 2) - Math.pow(y2, 2);

            double a = ((y1 - y2)*(n) - (y2 - y3)*(m)) / (2 * ((x2 - x3)*(y1 - y2) - (x1 - x2)*(y2 - y3)));
            double b = (m - 2 * a * (x1 - x2)) / (2 * (y1 - y2));

            double r = Math.sqrt(Math.pow(x1 - a, 2) + Math.pow(y1 - b, 2));

            return new Circle(new Point(a, b), r);
        }

        public Circle inscribedCircle(){

            double a = (this.AB * this.C.x() + this.BC * this.A.x() + this.AC * this.B.x()) / this.perimeter();
            double b = (this.AB * this.C.y() + this.BC * this.A.y() + this.AC * this.B.y()) / this.perimeter();
            double r = (2 * this.area()) / this.perimeter();

            return new Circle(new Point(a, b), r);
        }

        public Point orthocenter(){

            Point a = new Point(C.x() - B.x(), C.y() - B.y());
            Point b = new Point(C.x() - A.x(), C.y() - A.y());

            double k1 = A.x() * a.x() + A.y() * a.y();
            double k2 = B.x() * b.x() + B.y() * b.y();

            double n = a.x() * b.y() - b.x() * a.y();

            return new Point((k1 * b.y() - k2 * a.y()) / n, (k2 * a.x() - k1 * b.x()) / n);
        }

        public Circle ninePointsCircle(){

            Point circumcenter = this.circumscribedCircle().center();
            Point orthocenter = this.orthocenter();

            return new Circle(new Point((circumcenter.x() + orthocenter.x()) / 2,
                    (circumcenter.y() + orthocenter.y()) / 2),
                    this.circumscribedCircle().radius() / 2);

        }


        @Override
        public Point center() {
            return new Point((this.A.x() + this.B.x() + this.C.x()) / 3,
                    (this.A.y() + this.B.y() + this.C.y()) / 3);
        }

        @Override
        public double perimeter() {
            return this.AB + this.AC + this.BC;
        }

        @Override
        public double area() {
            double p = this.perimeter() / 2; // полупериметр

            return Math.sqrt(p * (p - this.AB) * (p - this.AC) * (p - this.BC));
        }

        @Override
        public void translate(Point newCenter) {
            double dx = this.center().x() - newCenter.x();
            double dy = this.center().y() - newCenter.y();

            this.A = new Point(this.A.x() - dx, this.A.y() - dy);
            this.B = new Point(this.B.x() - dx, this.B.y() - dy);
            this.C = new Point(this.C.x() - dx, this.C.y() - dy);
        }

        @Override
        public void rotate(double angle) {
            Point O = this.center();

            this.A = this.A.rotate(O, angle);
            this.B = this.B.rotate(O, angle);
            this.C = this.C.rotate(O, angle);
        }

        @Override
        public void scale(double coefficient) {
            Point O = this.center();
            this.A = this.A.scale(O, coefficient);
            this.B = this.B.scale(O, coefficient);
            this.C = this.C.scale(O, coefficient);
            this.AB = this.A.distance(this.B);
            this.BC = this.B.distance(this.C);
            this.AC = this.A.distance(this.C);
        }
    }


}