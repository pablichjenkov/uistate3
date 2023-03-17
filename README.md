### Under development 🚧👷
## Templato
Templato is a set of compose multiplatforms templates. The templates all target the 4 most popular user interfaced platforms, Browser, iOS, Android and the JVM(Mac, Window and Linux). The templates cover the UI for a certain business type of App, abstracting the data layer, so a variety of APIs for that specific business type can integrate.
<BR>
For instance, the Hotel Booking template provides the screens and navigation flows that a typical Hotel Booking App has. But, it depends on a data abstraction layer that a Hotel Booking company, will provide the implementation for. Once the specific data contracts implementations are provided, it is a matter of injecting it in the template and watch the App runs everywhere.
<BR>
There are also a couple of simple templates like [Hello World](https://github.com/pablichjenkov/templato/tree/master/hello-world), to help a person interested in this project, to understand how the components based design works. The templates build on top of the [Components](https://github.com/pablichjenkov/templato/wiki) tree design concept, which implementation lives in the **component-tree** module, check that module out for a better understanding of how to create new components or extend current components functionality.

### Running the templates
To run the projects you need to have the kotlin multiplatform environment setup in your working computer. For this matter visit the following link [Setup Environment](https://kotlinlang.org/docs/multiplatform-mobile-setup.html#install-the-necessary-tools). The page makes reference to an important cli tool called **kdoctor**. This tool will allow you to check if the environment is ready to build multiplatform Apps in kotlin. I recommend installing it.

---

### Components

[Component](https://github.com/pablichjenkov/templato/tree/master/uistate3) is the core module where the Components logic resides. Bellow are some screenshots of the demo App used to test the diferrent features available in the component library .

<table border="0">
 <tr>
    <td><b style="font-size:30px">Android</b></td>
    <td><b style="font-size:30px">iOS</b></td>
 </tr>
 <tr>
    <td><img title="Android components demo" src="https://user-images.githubusercontent.com/5303301/225816832-682d3620-6218-4d60-b742-4a692761abee.gif" alt="android-component-demo" width="300"></td>
    <td><img title="iOS components demo" src="https://user-images.githubusercontent.com/5303301/225282413-fb433cc2-416f-4d98-ad29-cb676030a0ec.gif" alt="ios-component-demo" width="300"></td>
 </tr>
</table>
<table border="0">
 <tr>
    <td><b style="font-size:30px">Web</b></td>
 </tr>
 <tr>
    <td><img title="Web components demo" src="https://user-images.githubusercontent.com/5303301/214518301-88398770-a508-45f2-b411-520155f4f7e9.jpg" alt="web-component-demo" width="800"></td>
 </tr>
</table>
<table border="0">
<tr>
    <td><b style="font-size:30px">Desktop</b></td>
 </tr>
 <tr>
    <td><img title="Desktop components demo" src="https://user-images.githubusercontent.com/5303301/225287289-d1870ba7-1a09-4570-a041-934746a35c11.gif" alt="desktop-component-demo" width="800"></td>
 </tr>
</table>

---

### Hello World

[Hello World](https://github.com/pablichjenkov/templato/tree/master/hello-world) is a simple example to get started with a components creation and configuration.

<table border="0">
 <tr>
    <td><b style="font-size:30px">Android</b></td>
    <td><b style="font-size:30px">iOS</b></td>
 </tr>
 <tr>
    <td><img title="Hello World Android" src="https://user-images.githubusercontent.com/5303301/215320502-0c771b1c-b5df-4181-aba0-7476d22e5995.jpg" alt="hello-world-android" width="300"></td>
    <td><img title="Hello World iOS" src="https://user-images.githubusercontent.com/5303301/214742102-878b386e-e324-433f-aee9-9c5629500ccc.jpg" alt="hello-world-ios" width="300"></td>
 </tr>
</table>
<table border="0">
 <tr>
    <td><b style="font-size:30px">Web</b></td>
 </tr>
 <tr>
    <td><img title="Hello World Web" src="https://user-images.githubusercontent.com/5303301/214742259-f912843a-cf85-4ce7-b69d-74b301eca6e5.jpg" alt="hello-world-web" width="800"></td>
 </tr>
</table>
