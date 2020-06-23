import axios from 'axios'
axios.defaults.baseURL = 'http://localhost:8082/api';

function download(url, downloadFileName) {
  axios({
    method: 'get',
    url: url,
    responseType: 'blob'
  }).then(response => {
    const responseData = response.data;
    let blob = new Blob([responseData], {type: 'application/vnd.ms-excel;charset=utf-8'});
    let url = window.URL.createObjectURL(blob);
    let aLink = document.createElement("a");
    aLink.style.display = "none";
    aLink.download = downloadFileName;
    aLink.href = url;
    document.body.appendChild(aLink);
    aLink.click();
  }).catch(error => {
    console.log(error)
  });
}

export {
  download
}
