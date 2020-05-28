<H1>Todo Application</H1>

  A sample android app that shows how to use ViewModels and Room together with Coroutine & Pure Depdency Injection, in Kotlin by Clean Architecture.
  
<b>Implemented by Clean Architecture</b>

<ul>
<li>Presentation (Activity/Fragment and ViewModel)</li>
<li>Domain (Repository Interface and Model classes)</li>
<li>Data (Local/remote datastore and Respository implementation)</li>
</ul>

<b>Dependecy between compoents</b>
 
 Activity/Fragment  --> ViewModel --> Repository --> LocalDataStore(Room DB) / RemoteDataSource (In Memory map)

<b>Communication between layers</b>
<ol>
  <li>UI calls method from ViewModel.</li>
  <li>ViewModel call the repo methods.</li>
  <li>Each Repository returns data from a Data Source (Cached or Remote).</li>
  <li>Information flows back to the UI where we display the list of Todos.</li>
</ol>
 
<b>Scenario</b>

At a glance:
<ul>
  <li>Use can either Register or Login if already registered</li>
  <li>Once authenticated, show list of todos with option to add new todo</li>
  <li>Once tap on each item, show edit/delete todo screen</li>
 </ul>

<b>References</b>

Creating dependencies lookup framework using buildsrc
https://handstandsam.com/2018/02/11/kotlin-buildsrc-for-better-gradle-dependency-management/
