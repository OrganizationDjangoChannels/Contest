import {ChangeEvent, Dispatch, FormEvent, SetStateAction} from "react";


type PaginationPropTypes = {
    page: SetStateAction<number>,
    setPage: Dispatch<SetStateAction<number>>,
}

const Pagination = ({page, setPage}: PaginationPropTypes) => {

    const handleOnChangePaginationForm = async (e: ChangeEvent<HTMLInputElement>)=> {
        setPage(Number(e.target.value));
    }

    const handleOnSubmitPaginationForm = async (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault();
    }

    const handleOnClickPrevious = () => {
        if (Number(page) > 0){
            setPage(prevState => prevState - 1)
        }
    }

    return (
        <>
            <div className={'pagination_container'}>
                <div className={'flex_pagination_container'}>
                    <div className={'pagination_element'}>
                        <button onClick={handleOnClickPrevious}>Prev</button>
                    </div>
                    <div className={'pagination_element'}>
                        <form onSubmit={handleOnSubmitPaginationForm} className={'pagination_form'}>
                            <input
                                type="number"
                                name='page'
                                className={'page_input'}
                                placeholder={'0'}
                                value={page ? String(page) : 0}
                                onChange={handleOnChangePaginationForm}
                            />
                        </form>
                    </div>
                    <div className={'pagination_element'}>
                        <button onClick={() => setPage(prevState => prevState + 1)}>Next</button>
                    </div>
                </div>
            </div>
        </>
    );
};

export default Pagination;