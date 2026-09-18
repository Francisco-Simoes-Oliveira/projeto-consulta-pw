# PrimeReact — Compact Documentation & Examples

> A single-file practical reference for PrimeReact, focused on the APIs and patterns most useful when building React applications.
>
> Official documentation: https://primereact.org/
>
> This is a compact reference, not a verbatim copy of the PrimeReact website. PrimeReact currently provides a large suite of React UI components, theming/styling options, TypeScript support, accessibility features, PrimeIcons, PrimeFlex and ready-made UI blocks.

---

## 1. What is PrimeReact?

PrimeReact is a React UI component library from PrimeTek.

It provides reusable components for:

- Buttons and actions
- Forms and inputs
- Data tables
- Dialogs and overlays
- Menus and navigation
- Messages and notifications
- Charts
- Pickers
- Panels
- Layouts
- Trees
- File upload
- Drag & drop
- Miscellaneous utilities

The current PrimeReact site advertises 80+ UI components and supports styled and unstyled approaches, themes, TypeScript, accessibility and responsive interfaces.

---

# 2. Installation

```bash
npm install primereact primeicons
```

or:

```bash
pnpm add primereact primeicons
```

```bash
yarn add primereact primeicons
```

```bash
bun add primereact primeicons
```

PrimeReact is used with React applications.

---

# 3. Basic Setup

A typical application imports the PrimeReact CSS/theme resources required by the chosen PrimeReact version.

Example:

```js
import "primereact/resources/themes/lara-light-blue/theme.css";
import "primereact/resources/primereact.min.css";
import "primeicons/primeicons.css";
```

Then:

```jsx
import { Button } from "primereact/button";

export default function App() {
  return <Button label="Hello PrimeReact" />;
}
```

> Theme/CSS setup differs between PrimeReact generations. Check the version-specific installation page when starting a new project.

---

# 4. Importing Components

PrimeReact components are generally imported from their component package path.

```js
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
```

Other examples:

```js
import { Dialog } from "primereact/dialog";
import { Dropdown } from "primereact/dropdown";
import { Calendar } from "primereact/calendar";
import { Checkbox } from "primereact/checkbox";
import { Toast } from "primereact/toast";
```

---

# 5. Core Mental Model

Most PrimeReact components follow this pattern:

```jsx
<Component
  value={value}
  onChange={event => setValue(event.value)}
  disabled={false}
  className="..."
/>
```

The important concepts are:

```text
Component
├── Props
├── Events / callbacks
├── Controlled state
├── Templates
├── Styling
├── Accessibility
└── Pass-through/customization options
```

---

# 6. Button

```jsx
import { Button } from "primereact/button";

<Button label="Save" />
```

Icon:

```jsx
<Button
  icon="pi pi-check"
  label="Save"
/>
```

Icon-only:

```jsx
<Button
  icon="pi pi-check"
  aria-label="Save"
/>
```

Severity:

```jsx
<Button label="Primary" />
<Button label="Secondary" severity="secondary" />
<Button label="Success" severity="success" />
<Button label="Info" severity="info" />
<Button label="Warning" severity="warning" />
<Button label="Danger" severity="danger" />
```

Outlined:

```jsx
<Button label="Save" outlined />
```

Text:

```jsx
<Button label="Save" text />
```

Rounded:

```jsx
<Button label="Save" rounded />
```

Loading:

```jsx
<Button
  label="Save"
  loading={loading}
/>
```

Event:

```jsx
<Button
  label="Save"
  onClick={() => save()}
/>
```

---

# 7. InputText

```jsx
import { InputText } from "primereact/inputtext";

const [name, setName] = useState("");

<InputText
  value={name}
  onChange={e => setName(e.target.value)}
/>
```

Placeholder:

```jsx
<InputText placeholder="Enter your name" />
```

Invalid:

```jsx
<InputText
  value={name}
  invalid={!name}
/>
```

Disabled:

```jsx
<InputText disabled />
```

---

# 8. InputNumber

```jsx
import { InputNumber } from "primereact/inputnumber";

const [value, setValue] = useState(0);

<InputNumber
  value={value}
  onValueChange={e => setValue(e.value)}
/>
```

Currency:

```jsx
<InputNumber
  value={price}
  onValueChange={e => setPrice(e.value)}
  mode="currency"
  currency="BRL"
  locale="pt-BR"
/>
```

Minimum/maximum:

```jsx
<InputNumber
  value={age}
  onValueChange={e => setAge(e.value)}
  min={0}
  max={120}
/>
```

---

# 9. Password

```jsx
import { Password } from "primereact/password";

<Password
  value={password}
  onChange={e => setPassword(e.target.value)}
/>
```

Toggle visibility:

```jsx
<Password toggleMask />
```

Feedback:

```jsx
<Password
  value={password}
  onChange={e => setPassword(e.target.value)}
  feedback
/>
```

---

# 10. Textarea

```jsx
import { InputTextarea } from "primereact/inputtextarea";

<InputTextarea
  value={description}
  onChange={e => setDescription(e.target.value)}
  rows={5}
/>
```

---

# 11. Checkbox

```jsx
import { Checkbox } from "primereact/checkbox";

const [checked, setChecked] = useState(false);

<Checkbox
  inputId="terms"
  checked={checked}
  onChange={e => setChecked(e.checked)}
/>
```

With label:

```jsx
<div>
  <Checkbox
    inputId="terms"
    checked={checked}
    onChange={e => setChecked(e.checked)}
  />

  <label htmlFor="terms">
    I accept the terms
  </label>
</div>
```

---

# 12. RadioButton

```jsx
import { RadioButton } from "primereact/radiobutton";

const [value, setValue] = useState("option1");

<RadioButton
  inputId="option1"
  name="option"
  value="option1"
  onChange={e => setValue(e.value)}
  checked={value === "option1"}
/>
```

---

# 13. Select / Dropdown

Depending on the PrimeReact version, selection components may be exposed under the classic `Dropdown` API or newer Select APIs.

Classic:

```jsx
import { Dropdown } from "primereact/dropdown";

const [selected, setSelected] = useState(null);

const cities = [
  { name: "Paranavaí", code: "PVA" },
  { name: "Maringá", code: "MGF" }
];

<Dropdown
  value={selected}
  onChange={e => setSelected(e.value)}
  options={cities}
  optionLabel="name"
  placeholder="Select a city"
/>
```

Object options:

```jsx
<Dropdown
  value={selected}
  options={cities}
  optionLabel="name"
  optionValue="code"
  onChange={e => setSelected(e.value)}
/>
```

---

# 14. MultiSelect

```jsx
import { MultiSelect } from "primereact/multiselect";

const [selectedCities, setSelectedCities] = useState([]);

<MultiSelect
  value={selectedCities}
  onChange={e => setSelectedCities(e.value)}
  options={cities}
  optionLabel="name"
  placeholder="Select cities"
/>
```

Useful properties include:

```text
filter
showClear
maxSelectedLabels
display
disabled
invalid
```

---

# 15. AutoComplete

```jsx
import { AutoComplete } from "primereact/autocomplete";

const [value, setValue] = useState(null);
const [items, setItems] = useState([]);

function search(event) {
  const query = event.query.toLowerCase();

  setItems(
    cities.filter(city =>
      city.name.toLowerCase().includes(query)
    )
  );
}

<AutoComplete
  value={value}
  suggestions={items}
  completeMethod={search}
  field="name"
  onChange={e => setValue(e.value)}
/>
```

Async search:

```jsx
async function search(event) {
  const response = await fetch(
    `/api/cities?q=${encodeURIComponent(event.query)}`
  );

  setItems(await response.json());
}
```

Current PrimeReact documentation also provides headless hooks for components such as AutoComplete, allowing you to control behavior while supplying your own markup/styling.

---

# 16. Calendar / DatePicker

Classic Calendar:

```jsx
import { Calendar } from "primereact/calendar";

const [date, setDate] = useState(null);

<Calendar
  value={date}
  onChange={e => setDate(e.value)}
/>
```

Date and time:

```jsx
<Calendar
  value={date}
  onChange={e => setDate(e.value)}
  showTime
  hourFormat="24"
/>
```

Range:

```jsx
<Calendar
  value={dates}
  onChange={e => setDates(e.value)}
  selectionMode="range"
/>
```

Multiple:

```jsx
<Calendar
  value={dates}
  onChange={e => setDates(e.value)}
  selectionMode="multiple"
/>
```

---

# 17. Forms

PrimeReact components work well with normal React state or form libraries.

Basic form:

```jsx
const [form, setForm] = useState({
  name: "",
  email: ""
});

function update(field, value) {
  setForm(prev => ({
    ...prev,
    [field]: value
  }));
}

<InputText
  value={form.name}
  onChange={e => update("name", e.target.value)}
/>

<InputText
  value={form.email}
  onChange={e => update("email", e.target.value)}
/>
```

Submit:

```jsx
<form onSubmit={handleSubmit}>
  ...
  <Button type="submit" label="Save" />
</form>
```

For larger applications, combine PrimeReact inputs with a dedicated React form/validation library.

---

# 18. Form Validation Pattern

```jsx
const [errors, setErrors] = useState({});

function validate() {
  const nextErrors = {};

  if (!form.name.trim()) {
    nextErrors.name = "Name is required";
  }

  if (!form.email.includes("@")) {
    nextErrors.email = "Invalid email";
  }

  setErrors(nextErrors);

  return Object.keys(nextErrors).length === 0;
}
```

```jsx
<InputText
  value={form.name}
  onChange={e => update("name", e.target.value)}
  invalid={!!errors.name}
/>

{errors.name && (
  <small className="p-error">
    {errors.name}
  </small>
)}
```

---

# 19. DataTable

One of the most useful PrimeReact components.

```jsx
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";

<DataTable value={users}>
  <Column field="id" header="ID" />
  <Column field="name" header="Name" />
  <Column field="email" header="Email" />
</DataTable>
```

---

# 20. DataTable Pagination

```jsx
<DataTable
  value={users}
  paginator
  rows={10}
  rowsPerPageOptions={[10, 25, 50]}
>
  <Column field="name" header="Name" />
  <Column field="email" header="Email" />
</DataTable>
```

---

# 21. DataTable Sorting

```jsx
<DataTable
  value={users}
  sortField="name"
  sortOrder={1}
>
  <Column
    field="name"
    header="Name"
    sortable
  />

  <Column
    field="email"
    header="Email"
  />
</DataTable>
```

Multiple sorting:

```jsx
<DataTable
  value={users}
  sortMode="multiple"
>
  <Column field="name" sortable />
  <Column field="age" sortable />
</DataTable>
```

---

# 22. DataTable Filtering

```jsx
<DataTable
  value={users}
  filterDisplay="row"
>
  <Column
    field="name"
    header="Name"
    filter
  />

  <Column
    field="email"
    header="Email"
    filter
  />
</DataTable>
```

Global filter:

```jsx
const [globalFilter, setGlobalFilter] = useState("");

<InputText
  value={globalFilter}
  onChange={e => setGlobalFilter(e.target.value)}
  placeholder="Search"
/>

<DataTable
  value={users}
  globalFilter={globalFilter}
>
  ...
</DataTable>
```

---

# 23. DataTable Selection

Single selection:

```jsx
const [selectedUser, setSelectedUser] = useState(null);

<DataTable
  value={users}
  selection={selectedUser}
  onSelectionChange={e => setSelectedUser(e.value)}
  selectionMode="single"
>
  <Column field="name" header="Name" />
</DataTable>
```

Multiple selection:

```jsx
const [selectedUsers, setSelectedUsers] = useState([]);

<DataTable
  value={users}
  selection={selectedUsers}
  onSelectionChange={e => setSelectedUsers(e.value)}
  selectionMode="multiple"
>
  ...
</DataTable>
```

---

# 24. DataTable Custom Body

```jsx
function statusBody(row) {
  return (
    <span>
      {row.active ? "Active" : "Inactive"}
    </span>
  );
}

<DataTable value={users}>
  <Column field="name" header="Name" />
  <Column
    field="active"
    header="Status"
    body={statusBody}
  />
</DataTable>
```

---

# 25. DataTable Action Column

```jsx
function actionsBody(row) {
  return (
    <div className="flex gap-2">
      <Button
        icon="pi pi-pencil"
        onClick={() => editUser(row)}
      />

      <Button
        icon="pi pi-trash"
        severity="danger"
        onClick={() => deleteUser(row)}
      />
    </div>
  );
}

<DataTable value={users}>
  <Column field="name" header="Name" />
  <Column body={actionsBody} header="Actions" />
</DataTable>
```

---

# 26. DataTable Lazy Loading

For server-side pagination/filtering:

```jsx
const [lazyParams, setLazyParams] = useState({
  first: 0,
  rows: 10,
  sortField: null,
  sortOrder: null,
  filters: {}
});

<DataTable
  value={users}
  lazy
  paginator
  first={lazyParams.first}
  rows={lazyParams.rows}
  totalRecords={totalRecords}
  onPage={event => {
    setLazyParams(prev => ({
      ...prev,
      first: event.first,
      rows: event.rows
    }));
  }}
  onSort={event => {
    setLazyParams(prev => ({
      ...prev,
      sortField: event.sortField,
      sortOrder: event.sortOrder
    }));
  }}
>
  ...
</DataTable>
```

The frontend then sends these values to the backend:

```js
api.get("/users", {
  params: {
    page: lazyParams.first / lazyParams.rows,
    size: lazyParams.rows,
    sort: lazyParams.sortField,
    direction: lazyParams.sortOrder
  }
});
```

---

# 27. Dialog

Classic-style usage:

```jsx
import { Dialog } from "primereact/dialog";

const [visible, setVisible] = useState(false);

<Button
  label="Open"
  onClick={() => setVisible(true)}
/>

<Dialog
  header="Edit User"
  visible={visible}
  onHide={() => setVisible(false)}
>
  <p>Dialog content</p>
</Dialog>
```

Current PrimeReact v11 documentation introduces a compositional Dialog API with parts such as:

```jsx
<Dialog>
  <Dialog.Trigger />
  <Dialog.Portal>
    <Dialog.Header>
      <Dialog.Title />
      <Dialog.HeaderActions>
        <Dialog.Maximizable />
        <Dialog.Close />
      </Dialog.HeaderActions>
    </Dialog.Header>

    <Dialog.Content />
    <Dialog.Footer />
  </Dialog.Portal>
</Dialog>
```

Use the API matching your installed PrimeReact version.

---

# 28. Dialog Position

Common positions include:

```text
center
top
top-left
top-right
bottom
bottom-left
bottom-right
left
right
```

Example:

```jsx
<Dialog
  visible={visible}
  position="top"
  onHide={() => setVisible(false)}
>
  Content
</Dialog>
```

---

# 29. ConfirmDialog

Useful for destructive actions.

```jsx
import { confirmDialog } from "primereact/confirmdialog";

confirmDialog({
  message: "Are you sure you want to delete this user?",
  header: "Confirmation",
  icon: "pi pi-exclamation-triangle",
  accept: () => deleteUser()
});
```

---

# 30. Toast

```jsx
import { Toast } from "primereact/toast";
import { useRef } from "react";

const toast = useRef(null);

<Toast ref={toast} />
```

Show a message:

```js
toast.current.show({
  severity: "success",
  summary: "Success",
  detail: "User created"
});
```

Common severities:

```text
success
info
warn
error
```

---

# 31. Messages

Inline message:

```jsx
import { Message } from "primereact/message";

<Message
  severity="info"
  text="Your changes have been saved."
/>
```

Error:

```jsx
<Message
  severity="error"
  text="Invalid credentials."
/>
```

---

# 32. ProgressBar

```jsx
import { ProgressBar } from "primereact/progressbar";

<ProgressBar value={65} />
```

Indeterminate:

```jsx
<ProgressBar mode="indeterminate" />
```

---

# 33. Tag

```jsx
import { Tag } from "primereact/tag";

<Tag value="Active" severity="success" />
```

Common severities:

```jsx
<Tag value="Info" severity="info" />
<Tag value="Warning" severity="warning" />
<Tag value="Danger" severity="danger" />
<Tag value="Success" severity="success" />
```

---

# 34. Badge

```jsx
import { Badge } from "primereact/badge";

<Badge value="5" />
```

---

# 35. Card

```jsx
import { Card } from "primereact/card";

<Card title="User">
  <p>User information...</p>
</Card>
```

Header/footer:

```jsx
<Card
  title="User"
  subTitle="Administrator"
  footer={<Button label="Edit" />}
>
  Content
</Card>
```

---

# 36. Panel

```jsx
import { Panel } from "primereact/panel";

<Panel header="Details">
  Content
</Panel>
```

Collapsible:

```jsx
<Panel
  header="Details"
  toggleable
>
  Content
</Panel>
```

---

# 37. Accordion

```jsx
import { Accordion, AccordionTab } from "primereact/accordion";

<Accordion>
  <AccordionTab header="First">
    First content
  </AccordionTab>

  <AccordionTab header="Second">
    Second content
  </AccordionTab>
</Accordion>
```

For newer versions, check whether the installed version uses the newer compositional Accordion API.

---

# 38. TabView / Tabs

Classic API:

```jsx
import { TabView, TabPanel } from "primereact/tabview";

<TabView>
  <TabPanel header="Profile">
    Profile
  </TabPanel>

  <TabPanel header="Settings">
    Settings
  </TabPanel>
</TabView>
```

---

# 39. Menu

```jsx
import { Menu } from "primereact/menu";
import { useRef } from "react";

const menu = useRef(null);

const items = [
  {
    label: "Options",
    items: [
      {
        label: "Refresh",
        icon: "pi pi-refresh"
      },
      {
        label: "Delete",
        icon: "pi pi-times"
      }
    ]
  }
];

<Menu model={items} ref={menu} />
```

---

# 40. Menubar

```jsx
import { Menubar } from "primereact/menubar";

<Menubar model={items} />
```

---

# 41. Toolbar

```jsx
import { Toolbar } from "primereact/toolbar";

<Toolbar
  start={
    <Button
      label="New"
      icon="pi pi-plus"
    />
  }
  end={
    <Button
      label="Export"
      icon="pi pi-upload"
    />
  }
/>
```

---

# 42. Breadcrumb

```jsx
import { BreadCrumb } from "primereact/breadcrumb";

const items = [
  { label: "Products" },
  { label: "Details" }
];

<BreadCrumb model={items} />
```

---

# 43. FileUpload

```jsx
import { FileUpload } from "primereact/fileupload";

<FileUpload
  name="file"
  url="/api/upload"
  accept="image/*"
  maxFileSize={1000000}
/>
```

Custom upload:

```jsx
<FileUpload
  name="file"
  customUpload
  uploadHandler={async event => {
    const form = new FormData();

    for (const file of event.files) {
      form.append("file", file);
    }

    await api.post("/upload", form);
  }}
/>
```

---

# 44. Image

```jsx
import { Image } from "primereact/image";

<Image
  src="/images/photo.jpg"
  alt="Photo"
  width="250"
/>
```

Preview:

```jsx
<Image
  src="/images/photo.jpg"
  alt="Photo"
  preview
  width="250"
/>
```

---

# 45. Avatar

```jsx
import { Avatar } from "primereact/avatar";

<Avatar
  label="E"
  shape="circle"
/>
```

Image:

```jsx
<Avatar
  image="/avatar.jpg"
  shape="circle"
/>
```

---

# 46. Skeleton

Useful while loading:

```jsx
import { Skeleton } from "primereact/skeleton";

<Skeleton width="10rem" height="2rem" />
```

DataTable-like loading:

```jsx
<Skeleton />
```

Combine skeletons with conditional rendering:

```jsx
{loading ? (
  <Skeleton height="2rem" />
) : (
  <span>{user.name}</span>
)}
```

---

# 47. OverlayPanel

```jsx
import { OverlayPanel } from "primereact/overlaypanel";
import { useRef } from "react";

const op = useRef(null);

<Button
  label="Options"
  onClick={e => op.current.toggle(e)}
/>

<OverlayPanel ref={op}>
  Overlay content
</OverlayPanel>
```

---

# 48. Tooltip

```jsx
import { Tooltip } from "primereact/tooltip";

<Tooltip target=".help-button" />

<Button
  className="help-button"
  icon="pi pi-question"
  data-pr-tooltip="More information"
/>
```

---

# 49. Sidebar

```jsx
import { Sidebar } from "primereact/sidebar";

const [visible, setVisible] = useState(false);

<Sidebar
  visible={visible}
  onHide={() => setVisible(false)}
>
  Menu content
</Sidebar>
```

Position:

```jsx
<Sidebar position="right" />
```

---

# 50. Drawer / Panel-like Overlays

For newer PrimeReact versions, check the current overlay component APIs because PrimeReact has been evolving toward compositional APIs.

Typical concepts remain:

```text
open/visible state
onOpenChange / onHide
position
modal
dismissable
close controls
```

---

# 51. Tree

```jsx
import { Tree } from "primereact/tree";

const nodes = [
  {
    key: "0",
    label: "Documents",
    children: [
      {
        key: "0-0",
        label: "Work"
      }
    ]
  }
];

<Tree
  value={nodes}
/>
```

Selection:

```jsx
const [selectedKey, setSelectedKey] = useState(null);

<Tree
  value={nodes}
  selectionMode="single"
  selectionKeys={selectedKey}
  onSelectionChange={e => setSelectedKey(e.value)}
/>
```

---

# 52. TreeTable

```jsx
import { TreeTable } from "primereact/treetable";
import { Column } from "primereact/column";

<TreeTable value={nodes}>
  <Column field="name" header="Name" expander />
  <Column field="size" header="Size" />
  <Column field="type" header="Type" />
</TreeTable>
```

---

# 53. Timeline

```jsx
import { Timeline } from "primereact/timeline";

<Timeline
  value={events}
  content={item => (
    <span>{item.status}</span>
  )}
/>
```

---

# 54. Paginator

```jsx
import { Paginator } from "primereact/paginator";

<Paginator
  first={first}
  rows={10}
  totalRecords={100}
  onPageChange={e => {
    setFirst(e.first);
  }}
/>
```

---

# 55. DataTable + Backend Pagination

Typical Spring Boot backend integration:

```jsx
async function loadUsers(event) {
  const page = event.first / event.rows;

  const response = await api.get("/users", {
    params: {
      page,
      size: event.rows
    }
  });

  setUsers(response.data.content);
  setTotalRecords(response.data.totalElements);
}
```

Then:

```jsx
<DataTable
  value={users}
  lazy
  paginator
  rows={10}
  totalRecords={totalRecords}
  onPage={loadUsers}
>
  <Column field="name" header="Name" />
  <Column field="email" header="Email" />
</DataTable>
```

This is a particularly useful pattern when PrimeReact is used with Spring Boot.

---

# 56. Templates

Many PrimeReact components allow templates/callbacks.

Example:

```jsx
<Column
  field="status"
  header="Status"
  body={row => (
    <Tag
      value={row.status}
      severity={
        row.status === "ACTIVE"
          ? "success"
          : "danger"
      }
    />
  )}
/>
```

General idea:

```text
Data
 ↓
PrimeReact component
 ↓
Template callback
 ↓
Your JSX
```

---

# 57. Styling

PrimeReact can be styled using:

- Theme styles
- CSS
- CSS modules
- Tailwind CSS
- PrimeFlex
- Inline styles
- Pass-through/customization APIs
- Unstyled mode

Example:

```jsx
<Button
  label="Save"
  className="my-button"
/>
```

CSS:

```css
.my-button {
  min-width: 10rem;
}
```

---

# 58. Inline Styles

```jsx
<Button
  label="Save"
  style={{
    minWidth: "10rem"
  }}
/>
```

React style objects follow normal React conventions.

---

# 59. Tailwind CSS

PrimeReact supports unstyled approaches that can be combined with utility-first CSS systems such as Tailwind.

Conceptually:

```jsx
<Button
  label="Save"
  className="px-4 py-2 rounded"
/>
```

For complete Tailwind integration, use the PrimeReact version's current unstyled/Tailwind documentation.

---

# 60. PrimeFlex

PrimeFlex is PrimeTek's CSS utility library.

Example:

```html
<div class="flex align-items-center justify-content-between">
  ...
</div>
```

Common utility categories include:

```text
flex
grid
spacing
alignment
width/height
typography
borders
shadows
responsive utilities
```

PrimeFlex is separate from PrimeReact itself.

---

# 61. Icons

Install:

```bash
npm install primeicons
```

Import:

```js
import "primeicons/primeicons.css";
```

Use an icon:

```jsx
<i className="pi pi-check" />
```

Button:

```jsx
<Button
  icon="pi pi-check"
  label="Save"
/>
```

Common examples:

```text
pi pi-check
pi pi-times
pi pi-plus
pi pi-minus
pi pi-pencil
pi pi-trash
pi pi-search
pi pi-user
pi pi-home
pi pi-calendar
pi pi-download
pi pi-upload
pi pi-refresh
```

---

# 62. Accessibility

PrimeReact components are designed with accessibility in mind.

Still provide meaningful application-level information.

Good:

```jsx
<Button
  icon="pi pi-trash"
  aria-label="Delete user"
/>
```

Good form association:

```jsx
<label htmlFor="email">
  Email
</label>

<InputText id="email" />
```

Dialog accessibility includes keyboard interaction and ARIA semantics. The current Dialog documentation describes dialog roles, labels, modal behavior, and Escape/Tab keyboard handling.

---

# 63. Controlled vs Uncontrolled State

Prefer controlled state when application state matters.

Controlled:

```jsx
const [value, setValue] = useState("");

<InputText
  value={value}
  onChange={e => setValue(e.target.value)}
/>
```

Conceptually:

```text
React state
    ↓
component value
    ↓
user interaction
    ↓
onChange
    ↓
React state
```

This makes form state predictable.

---

# 64. API Calls + Loading State

```jsx
const [users, setUsers] = useState([]);
const [loading, setLoading] = useState(false);

async function loadUsers() {
  setLoading(true);

  try {
    const { data } = await api.get("/users");
    setUsers(data);
  } finally {
    setLoading(false);
  }
}
```

DataTable:

```jsx
<DataTable
  value={users}
  loading={loading}
>
  ...
</DataTable>
```

---

# 65. API Error + Toast

```jsx
async function saveUser(user) {
  try {
    await api.post("/users", user);

    toast.current.show({
      severity: "success",
      summary: "Success",
      detail: "User created"
    });
  } catch (error) {
    toast.current.show({
      severity: "error",
      summary: "Error",
      detail: "Could not create user"
    });
  }
}
```

---

# 66. CRUD Screen Pattern

A common PrimeReact CRUD page:

```text
Toolbar
   ↓
DataTable
   ├── Pagination
   ├── Sorting
   ├── Filtering
   └── Actions
          ↓
       Dialog
          ↓
        Form
          ↓
       API service
          ↓
       Backend
```

Example structure:

```text
pages/
└── Users/
    ├── UsersPage.jsx
    ├── UserDialog.jsx
    ├── users.service.js
    └── users.schema.js
```

---

# 67. Recommended React Architecture

```text
src/
├── api/
│   └── axios.js
├── components/
│   ├── UserDialog.jsx
│   └── UserTable.jsx
├── pages/
│   └── Users/
│       └── UsersPage.jsx
├── services/
│   └── users.service.js
├── hooks/
│   └── useUsers.js
└── App.jsx
```

PrimeReact should generally remain a UI layer rather than becoming the place where business logic lives.

---

# 68. Example: Complete CRUD Page

```jsx
import { useEffect, useRef, useState } from "react";
import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { Dialog } from "primereact/dialog";
import { InputText } from "primereact/inputtext";
import { Toast } from "primereact/toast";

import { api } from "./api";

export default function UsersPage() {
  const toast = useRef(null);

  const [users, setUsers] = useState([]);
  const [user, setUser] = useState({
    name: "",
    email: ""
  });

  const [dialogVisible, setDialogVisible] =
    useState(false);

  const [loading, setLoading] = useState(false);

  async function loadUsers() {
    setLoading(true);

    try {
      const { data } = await api.get("/users");
      setUsers(data);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadUsers();
  }, []);

  function openNew() {
    setUser({
      name: "",
      email: ""
    });

    setDialogVisible(true);
  }

  async function saveUser() {
    await api.post("/users", user);

    setDialogVisible(false);

    toast.current.show({
      severity: "success",
      summary: "Success",
      detail: "User created"
    });

    loadUsers();
  }

  return (
    <>
      <Toast ref={toast} />

      <div className="flex justify-content-between mb-3">
        <h2>Users</h2>

        <Button
          label="New"
          icon="pi pi-plus"
          onClick={openNew}
        />
      </div>

      <DataTable
        value={users}
        loading={loading}
      >
        <Column field="id" header="ID" />
        <Column field="name" header="Name" />
        <Column field="email" header="Email" />
      </DataTable>

      <Dialog
        header="New User"
        visible={dialogVisible}
        onHide={() => setDialogVisible(false)}
      >
        <div className="flex flex-column gap-3">
          <InputText
            value={user.name}
            placeholder="Name"
            onChange={e =>
              setUser({
                ...user,
                name: e.target.value
              })
            }
          />

          <InputText
            value={user.email}
            placeholder="Email"
            onChange={e =>
              setUser({
                ...user,
                email: e.target.value
              })
            }
          />

          <Button
            label="Save"
            onClick={saveUser}
          />
        </div>
      </Dialog>
    </>
  );
}
```

---

# 69. Headless / Unstyled Approach

PrimeReact has been expanding its headless APIs.

The idea is:

```text
PrimeReact
   ↓
Behavior/state/accessibility
   ↓
Your own markup
   ↓
Your own CSS/design system
```

This is useful when you want PrimeReact behavior without committing to the default visual styling.

The current v11 documentation includes hooks such as:

```js
import { useAutoComplete } from "@primereact/headless/autocomplete";
```

Example concept:

```jsx
const autocomplete = useAutoComplete({
  options,
  onComplete: search
});

const {
  rootProps,
  inputProps,
  triggerProps,
  listProps,
  popupProps
} = autocomplete;
```

You then render your own HTML around those props.

---

# 70. PrimeReact v11 API Style

Current v11 documentation introduces more compositional APIs for some components.

Example:

```jsx
<Dialog>
  <Dialog.Trigger>
    Open
  </Dialog.Trigger>

  <Dialog.Portal>
    <Dialog.Header>
      <Dialog.Title>
        Edit Profile
      </Dialog.Title>

      <Dialog.HeaderActions>
        <Dialog.Close />
      </Dialog.HeaderActions>
    </Dialog.Header>

    <Dialog.Content>
      ...
    </Dialog.Content>
  </Dialog.Portal>
</Dialog>
```

This differs from older APIs such as:

```jsx
<Dialog
  visible={visible}
  onHide={() => setVisible(false)}
  header="Edit Profile"
>
  ...
</Dialog>
```

**Do not mix APIs blindly.** Check the documentation for the exact PrimeReact version installed in your project.

---

# 71. Version Awareness

PrimeReact APIs evolve.

Before copying an example, check:

```bash
npm list primereact
```

Then consult the corresponding documentation version.

For example:

```bash
npm install primereact@latest
```

does not mean an example from an older major version will necessarily be identical.

Pay particular attention to:

- Dialog
- Tabs
- Accordion
- Select components
- Theming
- Unstyled mode
- Pass Through
- Headless APIs

---

# 72. PrimeReact + TypeScript

PrimeReact has TypeScript support.

Example:

```tsx
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";

type User = {
  id: number;
  name: string;
  email: string;
};

function UsersTable({ users }: { users: User[] }) {
  return (
    <DataTable value={users}>
      <Column field="id" header="ID" />
      <Column field="name" header="Name" />
      <Column field="email" header="Email" />
    </DataTable>
  );
}
```

Typed event handlers can use the exported component event types where needed.

---

# 73. PrimeReact + Axios

PrimeReact handles UI; Axios handles HTTP.

Recommended separation:

```text
PrimeReact
   ↓
React component
   ↓
Service / hook
   ↓
Axios
   ↓
Spring Boot / API
```

Example:

```js
// users.service.js
import { api } from "./api";

export async function getUsers(params) {
  const { data } = await api.get("/users", {
    params
  });

  return data;
}
```

Component:

```jsx
useEffect(() => {
  getUsers({
    page: 0,
    size: 10
  }).then(setUsers);
}, []);
```

---

# 74. PrimeReact + Spring Boot

A common full-stack combination:

```text
React
├── PrimeReact
├── Axios
└── React Router

        HTTP/JSON

Spring Boot
├── Controller
├── Service
├── Repository
└── Database
```

Example endpoint:

```http
GET /api/users?page=0&size=10
```

Frontend:

```js
const { data } = await api.get("/users", {
  params: {
    page: 0,
    size: 10
  }
});
```

Spring Boot:

```java
@GetMapping("/users")
public Page<User> findAll(Pageable pageable) {
    return repository.findAll(pageable);
}
```

This works naturally with PrimeReact's lazy DataTable model.

---

# 75. Useful Component Selection Guide

| Need | Component |
|---|---|
| Button/action | `Button` |
| Text input | `InputText` |
| Number | `InputNumber` |
| Password | `Password` |
| Multiline text | `InputTextarea` |
| Boolean | `Checkbox` |
| One option | `RadioButton` |
| Select one | `Dropdown` / version-specific Select |
| Select many | `MultiSelect` |
| Search suggestions | `AutoComplete` |
| Date | `Calendar` / version-specific DatePicker |
| Table | `DataTable` |
| Tree table | `TreeTable` |
| Modal | `Dialog` |
| Confirmation | `ConfirmDialog` |
| Notification | `Toast` |
| Inline status | `Message` |
| Label/status | `Tag` |
| Loading | `ProgressBar` / `Skeleton` |
| Side panel | `Sidebar` |
| Menu | `Menu` |
| Top navigation | `Menubar` |
| Actions row | `Toolbar` |
| File upload | `FileUpload` |
| Image preview | `Image` |
| Hierarchical data | `Tree` |
| Pagination | `Paginator` |
| Breadcrumb | `BreadCrumb` |
| Tooltip | `Tooltip` |
| Overlay | `OverlayPanel` |
| User avatar | `Avatar` |
| Content container | `Card` / `Panel` |
| Steps/tabs | version-specific Tabs/TabView |

---

# 76. Common Component Props

Many PrimeReact components commonly expose concepts such as:

```text
value
onChange
disabled
invalid
className
style
id
name
aria-*
```

Not every component supports every property.

Always use the component-specific API for exact prop names and event types.

---

# 77. Common Events

Typical React/PrimeReact event patterns:

```jsx
onChange={e => ...}
onClick={e => ...}
onHide={() => ...}
onShow={() => ...}
onSelect={e => ...}
onPage={e => ...}
onSort={e => ...}
onFilter={e => ...}
onSelectionChange={e => ...}
```

PrimeReact event objects commonly expose the new value through:

```js
e.value
```

For native input events, use:

```js
e.target.value
```

This distinction is important.

Example:

```jsx
<InputText
  onChange={e => setName(e.target.value)}
/>

<Dropdown
  onChange={e => setCity(e.value)}
/>
```

---

# 78. Common Mistakes

### 1. Mixing PrimeReact major versions

An example from one major version may not match another.

### 2. Treating PrimeReact as business logic

Keep API calls, validation rules and domain logic outside reusable UI components when possible.

### 3. Ignoring loading/error states

Use:

```text
loading
error
empty
success
```

as explicit UI states.

### 4. Missing accessibility labels

Especially for icon-only buttons:

```jsx
<Button
  icon="pi pi-trash"
  aria-label="Delete"
/>
```

### 5. Forgetting server-side pagination

Large datasets should generally not be downloaded entirely just to paginate in the browser.

### 6. Over-customizing individual components

Establish a consistent theme/design system first.

---

# 79. Production CRUD Checklist

For a typical CRUD page:

```text
[ ] Axios API instance
[ ] API service
[ ] Loading state
[ ] Error state
[ ] Empty state
[ ] DataTable
[ ] Server pagination
[ ] Sorting
[ ] Filtering
[ ] Selection
[ ] Create dialog
[ ] Edit dialog
[ ] Delete confirmation
[ ] Toast feedback
[ ] Form validation
[ ] Accessibility labels
[ ] Responsive layout
```

---

# 80. Minimal Production Setup

```js
// api.js
import axios from "axios";

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
  timeout: 10000
});
```

```jsx
// UsersPage.jsx
import { useEffect, useState } from "react";
import { Button } from "primereact/button";
import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";

import { api } from "./api";

export default function UsersPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(false);

  async function loadUsers() {
    setLoading(true);

    try {
      const { data } = await api.get("/users");
      setUsers(data);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadUsers();
  }, []);

  return (
    <DataTable
      value={users}
      loading={loading}
      paginator
      rows={10}
    >
      <Column field="id" header="ID" />
      <Column field="name" header="Name" />
      <Column field="email" header="Email" />
    </DataTable>
  );
}
```

---

# 81. Quick Reference

## Most common imports

```js
import { Button } from "primereact/button";
import { InputText } from "primereact/inputtext";
import { InputNumber } from "primereact/inputnumber";
import { Dropdown } from "primereact/dropdown";
import { MultiSelect } from "primereact/multiselect";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Dialog } from "primereact/dialog";
import { Toast } from "primereact/toast";
import { ConfirmDialog } from "primereact/confirmdialog";
import { Calendar } from "primereact/calendar";
import { Checkbox } from "primereact/checkbox";
import { FileUpload } from "primereact/fileupload";
```

## Most common DataTable pattern

```jsx
<DataTable
  value={data}
  paginator
  rows={10}
>
  <Column
    field="name"
    header="Name"
    sortable
    filter
  />
</DataTable>
```

## Most common controlled input

```jsx
<InputText
  value={value}
  onChange={e => setValue(e.target.value)}
/>
```

## Most common PrimeReact selection input

```jsx
<Dropdown
  value={value}
  options={options}
  onChange={e => setValue(e.value)}
/>
```

## Most common modal

```jsx
<Dialog
  visible={visible}
  onHide={() => setVisible(false)}
>
  Content
</Dialog>
```

## Most common notification

```js
toast.current.show({
  severity: "success",
  summary: "Success",
  detail: "Saved successfully"
});
```

---

# 82. Documentation Map

```text
PrimeReact
├── Getting Started
│   ├── Installation
│   ├── Setup
│   ├── Themes
│   └── Accessibility
│
├── Components
│   ├── Button
│   ├── Inputs
│   ├── Selects
│   ├── DataTable
│   ├── Dialogs
│   ├── Overlays
│   ├── Menus
│   ├── Panels
│   ├── Trees
│   ├── FileUpload
│   └── Utilities
│
├── Styling
│   ├── Themes
│   ├── Styled mode
│   ├── Unstyled mode
│   ├── Tailwind
│   ├── PrimeFlex
│   └── Pass Through
│
├── Advanced
│   ├── Templates
│   ├── Headless APIs
│   ├── Accessibility
│   ├── TypeScript
│   └── Performance
│
└── Ecosystem
    ├── PrimeIcons
    ├── PrimeFlex
    ├── PrimeBlocks
    └── Templates
```

---

# 83. Official References

- PrimeReact: https://primereact.org/
- Current v10 documentation: https://v10.primereact.org/
- Current v11 documentation: https://v11.primereact.org/
- PrimeReact components: https://primereact.org/
- PrimeIcons: https://primereact.org/icons/
- PrimeFlex: https://primeflex.org/
- PrimeBlocks: https://blocks.primereact.org/

---

## Version note

PrimeReact's API is actively evolving. The official current v11 documentation includes newer compositional and headless APIs, while many existing applications still use the v9/v10-style APIs. Always match examples to the major version installed in your project.

Official PrimeReact currently describes the library as a React UI suite with 80+ components and styled/unstyled approaches, while the v11 documentation includes headless hooks such as `useAutoComplete` and compositional components such as the newer `Dialog` API.
